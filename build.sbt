// Dependency versions
val pureconfigVer = "0.17.1"
val loggingVer = "3.9.2"
val logbackVer = "1.2.10"
val testVer = "3.2.11"

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

val dependencies = 
  pureconfig ++ 
  logging ++
  test

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