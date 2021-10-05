import scala.collection.mutable.ListBuffer
import scala.concurrent.{
  Await,
  ExecutionContext,
  Future
}
import scala.concurrent.duration.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.{
  postfixOps
}
import org.scalatest.funsuite.{
  AnyFunSuite
}
import fpsample_scala.lib.{
  Data,
  DataReader,
  DataWriter,
  runApp
}


enum TestEvent:
  case ReadCalled
  case WriteCalled

import TestEvent.*

case class TestState(
  events: ListBuffer[TestEvent] = ListBuffer[TestEvent]()
)

case class TestApp(state: TestState)

given DataReader[TestApp] with
  extension (app: TestApp)
    def read(using ExecutionContext): Future[Data] =
      app.state.events += ReadCalled
      Future.successful(Data("foo", 1))

given DataWriter[TestApp] with
  extension (app: TestApp)
    def write(data: Data)(using ExecutionContext): Future[Unit] =
      app.state.events += WriteCalled
      Future.successful(())

class SetSuite extends AnyFunSuite:
  test("readData and writeData are called correctly") {
    val state = TestState()
    Await.ready(runApp(TestApp(state)), Duration.Inf)
    assertResult(List(ReadCalled, WriteCalled))(state.events.toList)
  }
