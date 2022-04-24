import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"

run / fork := true

scalacOptions ++= Seq(
  "-Ymacro-annotations"
)

lazy val root = (project in file("."))
  .settings(
    name := "http-title-crawler",
    libraryDependencies ++= Seq (
      catsEffect,
      fs2,
      scraper,
      circe,
      newtype
    ) ++ http4s
  )
