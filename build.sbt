import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.byoms23",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "exchange-rate-gt",

    libraryDependencies ++= Seq(
      scalaTest % Test,
      // For Akka 2.4.x or 2.5.x
      "com.typesafe.akka" %% "akka-http" % "10.0.10",
      // Only when running against Akka 2.5 explicitly depend on akka-streams in same version as akka-actor
      "com.typesafe.akka" %% "akka-stream" % "2.5.4",
      "com.typesafe.akka" %% "akka-actor"  % "2.5.4",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10"
    )
  )
