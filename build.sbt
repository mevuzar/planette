name := "planette"

organization := "com.mayo"

version := "1.0"

scalaVersion := "2.11.8"

//akkaVersion = "2.4.7"

libraryDependencies ++= Seq(
  "com.mayo" %% "what-is" % "SNAPSHOT-0.1.0",
  "org.scalacheck" % "scalacheck_2.11" % "1.13.1",
  "org.scalaz" %% "scalaz-core" % "7.1.0",
  "org.scalaz" %% "scalaz-concurrent" % "7.1.0",
  "com.typesafe.akka" %% "akka-actor" % "2.4.7",
  "com.typesafe.akka" %% "akka-stream" % "2.4.7",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "org.json4s" %% "json4s-core" % "3.3.0",
  "org.json4s" %% "json4s-ext"  % "3.3.0",
  "org.scala-lang.modules" %% "scala-pickling" % "0.10.1"
)
    