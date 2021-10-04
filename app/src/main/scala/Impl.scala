package fpsample_scala.app

import scala.compat.java8.{
  FutureConverters
}
import scala.concurrent.{
  ExecutionContext,
  Future
}
import java.net.{
  URI
}
import java.net.http.{
  HttpClient,
  HttpResponse,
  HttpRequest
}
import io.circe.generic.auto.*
import io.circe.parser.{
  decode
}
import fpsample_scala.lib.{
  Data,
  DataReader,
  DataWriter
}

case class App(url: String)

given DataReader[App] with
  extension (app: App)
    def read(using ExecutionContext): Future[Data] =
      val client = HttpClient.newHttpClient()
      val req    = HttpRequest
        .newBuilder
        .uri(URI.create(app.url))
        .build
      
      for
        res <- FutureConverters.toScala(
          client
            .sendAsync(req, HttpResponse.BodyHandlers.ofString())
        )
      yield
        decode[Data](res.body) match
          case Left(e) =>
            Console.err.println(e)
            throw IllegalStateException()
          case Right(data) => data

given DataWriter[App] with
  extension (app: App)
    def write(data: Data)(using ExecutionContext): Future[Unit] =
      println(s"name=${data.name}")
      println(s"age=${data.age}")
      Future.successful(())
