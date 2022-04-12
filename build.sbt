import Libs._

val dependencies = 
  pureconfig ++ 
  logging ++
  scalatest ++
  akka ++
  storm ++
  kafka ++
  embeddedKafka ++
  langDetect ++
  circe ++
  twitter4j ++
  stanfordNlp

lazy val settings   = Seq(
  scalaVersion := "2.13.8",
  organization := "com.alawresz"
)

lazy val testConfig = Seq(
  Test / fork := true
)

lazy val root = (project in file("."))
  .settings(settings)
  .settings(
    name := "twitterment",
    version := "0.5",
    resolvers ++= Seq(
      "clojure" at "https://repo.clojars.org"
    ),
    libraryDependencies ++= dependencies,
    dependencyOverrides ++= dependenciesToOverride
  )
  .settings(testConfig)

 assembly / assemblyMergeStrategy := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}