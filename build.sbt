name := "planette"

organization := "com.mayo"

version := "1.0"

scalaVersion := "2.11.8"
val akkaVersion = "2.4.7"

libraryDependencies ++= Seq(
  "com.mayo" %% "whatis" % "SNAPSHOT-0.1.0",
  "org.scalacheck" % "scalacheck_2.11" % "1.13.1",
  "org.scalaz" %% "scalaz-core" % "7.1.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)
    