import Libs._

val dependencies = 
  pureconfig ++ 
  logging ++
  scalatest ++
  akka ++
  storm ++
  kafka ++
  langDetect ++
  circe ++
  twitter4j ++
  stanfordNlp

lazy val settings = Seq(
  scalaVersion := "2.13.8",
  organization := "com.alawresz",
  fork := true
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