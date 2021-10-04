package fpsample_scala.app

import scala.concurrent.{
  Await
}
import scala.concurrent.duration.*
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.{
  postfixOps
}
import scopt.{
  OParser
}
import fpsample_scala.lib.{
  runApp
}

case class Options(url: String = "")

@main
def main(args: String*): Unit = 
  val builder = OParser.builder[Options]
  val parser  = OParser.sequence(
    builder.opt[String]("url")
      .required()
      .action((x, opt) => opt.copy(url = x))
      .text("Set url for reading data.")
  )

  OParser.parse(parser, args, Options()) match
    case Some(options) =>
      Await.ready(runApp(App(options.url)), Duration.Inf)
    case _ => ()
