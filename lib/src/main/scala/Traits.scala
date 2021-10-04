package fpsample_scala.lib

import scala.concurrent.{
  ExecutionContext,
  Future
}

trait DataReader[App]:
  extension (app: App)
    def read(using ExecutionContext): Future[Data]

trait DataWriter[App]:
  extension (app: App)
    def write(data: Data)(using ExecutionContext): Future[Unit]
