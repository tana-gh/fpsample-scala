package fpsample_scala.lib

import scala.concurrent.{
  ExecutionContext,
  Future
}

def runApp[App](app: App)(using DataReader[App], DataWriter[App], ExecutionContext): Future[Unit] =
  for
    data <- app.read
    ()   <- app.write(data)
  yield ()
