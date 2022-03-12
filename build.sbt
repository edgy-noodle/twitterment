// val dependencies = Seq(???)
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
    // libraryDependencies ++= dependencies
  )