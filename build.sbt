ThisBuild / scalaVersion := "3.0.2"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val root = project
  .in(file("."))
  .aggregate(lib, app)

lazy val lib = project
  .in(file("lib"))
  .settings(
    name := "fpsample-scala.lib",
  )

lazy val app = project
  .in(file("app"))
  .dependsOn(lib)
  .settings(
    name := "fpsample-scala.app",
    libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.1",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % "0.15.0-M1"),
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.1",

    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test
  )
  