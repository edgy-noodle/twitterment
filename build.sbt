import Libs._

val dependencies = 
  pureconfig ++ 
  logging ++
  akka ++
  storm ++
  stormMetrics ++
  kafka ++
  langDetect ++
  circe ++
  twitter4j ++
  stanfordNlp ++
  scalatest ++
  embeddedKafka

lazy val settings = Seq(
  scalaVersion := "2.13.8",
  organization := "com.alawresz"
)

lazy val root = (project in file("."))
  .settings(settings)
  .settings(
    name := "twitterment",
    version := "0.5",
    resolvers ++= Seq(
      "clojure" at "https://repo.clojars.org"
    ),
    libraryDependencies ++= dependencies
  )