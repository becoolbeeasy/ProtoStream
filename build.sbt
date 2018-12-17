name := "TrackStreamer"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "io.spray" %%  "spray-json" % "1.3.4",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.3"
)