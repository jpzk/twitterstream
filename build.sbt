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

lazy val twitterstream = (project in file("twitterstream")).
  settings(commonSettings: _*).
  settings(
    assemblyJarName in assembly := "twitterstream.jar"
  ).
  settings(
    resolvers ++= Seq(
      "twttr" at "http://maven.twttr.com/",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
    )
  ).settings(libraryDependencies ++= Seq(
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.3",
  "com.twitter" %% "util-core" % twitterVersion,
  "com.twitter" % "hbc-core" % hbcCoreVersion,
  "org.apache.kafka" % "kafka-streams" % kafkaStreamsVersion,
  "com.google.code.gson" % "gson" % gsonVersion,
  "com.typesafe" % "config" % configVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
))


