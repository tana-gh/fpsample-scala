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

case class TestState(
  var readCalled : Boolean = false,
  var writeCalled: Boolean = false
)

case class TestApp(state: TestState)

given DataReader[TestApp] with
  extension (app: TestApp)
    def read(using ExecutionContext): Future[Data] =
      app.state.readCalled = true
      Future.successful(Data("foo", 1))

given DataWriter[TestApp] with
  extension (app: TestApp)
    def write(data: Data)(using ExecutionContext): Future[Unit] =
      app.state.writeCalled = true
      Future.successful(())

class SetSuite extends AnyFunSuite:
  test("readData and writeData are called correctly") {
    val state = TestState()
    Await.ready(runApp(TestApp(state)), Duration.Inf)
    assert(state.readCalled)
    assert(state.writeCalled)
  }
