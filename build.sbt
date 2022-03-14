// Dependency versions
val pureconfigVer = "0.17.1"
val loggingVer = "3.9.2"
val logbackVer = "1.2.10"
val testVer = "3.2.11"
val akkaVer = "2.6.8"
val akkaHttpVer = "10.2.9"
val stormVer = "2.3.0"

// Dependencies
val pureconfig = Seq(
  "com.github.pureconfig" %% "pureconfig" % pureconfigVer
  )
val logging = Seq(
  "ch.qos.logback" % "logback-classic" % logbackVer,
  "com.typesafe.scala-logging" %% "scala-logging" % loggingVer
)
val test = Seq(
  "org.scalatest" %% "scalatest" % testVer,
  "org.scalatest" %% "scalatest" % testVer % "test"
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

val dependencies = 
  pureconfig ++ 
  logging ++
  test ++
  akka ++
  storm

lazy val settings = Seq(
  scalaVersion := "2.13.8",
  organization := "com.alawresz"
)

lazy val root = (project in file("."))
  .settings(settings)
  .settings(
    name := "name",
    version := "0.1",
    resolvers ++= Seq(
      "clojure" at "https://repo.clojars.org"
    ),
    libraryDependencies ++= dependencies
  )