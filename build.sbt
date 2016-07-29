lazy val commonSettings = Seq(
  version := "0.1-SNAPSHOT",
  organization := "mwt",
  scalaVersion := "2.11.8"
)

val twitterVersion = "6.34.0"
val kafkaStreamsVersion = "0.10.0.0"
val hbcCoreVersion = "2.2.0"
val gsonVersion = "2.7"
val configVersion = "1.0.1"
val scalaLoggingVersion = "3.4.0"

lazy val ingest = (project in file("ingest")).
  settings(commonSettings: _*).
  settings(
    mainClass in assembly := Some("mwt.twitterstream.IngestApp")
  ).
  settings(
    assemblyJarName in assembly := "ingest.jar"
  ).
  settings(libraryDependencies ++= Seq(
    "com.twitter" %% "util-core" % twitterVersion,
    "com.twitter" % "hbc-core" % hbcCoreVersion,
    "org.apache.kafka" % "kafka-streams" % kafkaStreamsVersion,
    "com.google.code.gson" % "gson" % gsonVersion,
    "com.typesafe" % "config" % configVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  ))


