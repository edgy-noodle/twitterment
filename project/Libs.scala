import sbt._

object Libs {
  // Dependency versions
  private val pureconfigVer = "0.17.1"
  private val loggingVer = "3.9.2"
  private val logbackVer = "1.2.10"
  private val scalatestVer = "3.2.11"
  private val akkaVer = "2.6.8"
  private val akkaHttpVer = "10.2.9"
  private val stormVer = "2.3.0"
  private val kafkaVer = "3.1.0"
  private val langDetectVer = "0.6"
  private val circeVer = "0.14.1"
  private val twitter4jVer = "4.0.7"

  // Dependencies
  val pureconfig = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureconfigVer
    )
  val logging = Seq(
    // "ch.qos.logback" % "logback-classic" % logbackVer,
    "com.typesafe.scala-logging" %% "scala-logging" % loggingVer
  )
  val scalatest = Seq(
    "org.scalatest" %% "scalatest" % scalatestVer,
    "org.scalatest" %% "scalatest" % scalatestVer % "test"
  )
  val akka = Seq(
    "com.typesafe.akka" %% "akka-stream" % akkaVer,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVer,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVer
  )
  val storm = Seq(
    "org.apache.storm" % "storm-core",
    "org.apache.storm" % "storm-client",
    "org.apache.storm" % "storm-kafka-client"
  ).map(_ % stormVer)
  val kafka = Seq(
    "org.apache.kafka" % "kafka-clients" % kafkaVer
  )
  val langDetect = Seq(
    "com.optimaize.languagedetector" % "language-detector" % langDetectVer
  )
  val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVer)
  val twitter4j = Seq(
    "org.twitter4j" % "twitter4j-stream" % twitter4jVer
  )
}
