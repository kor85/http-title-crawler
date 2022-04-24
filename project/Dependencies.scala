import sbt._

object Dependencies {
  lazy val catsEffect = "org.typelevel"    %% "cats-effect"   % "3.3.11"
  lazy val fs2        = "co.fs2"           %% "fs2-core"      % "3.2.7"
  lazy val scraper    = "net.ruippeixotog" %% "scala-scraper" % "2.2.1"
  lazy val http4s = Seq(
    "org.http4s" %% "http4s-server"       % "0.23.11",
    "org.http4s" %% "http4s-dsl"          % "0.23.11",
    "org.http4s" %% "http4s-blaze-server" % "0.23.11",
    "org.http4s" %% "http4s-circe"        % "0.23.11"
  )
  lazy val circe   = "io.circe"    %% "circe-generic" % "0.14.1"
  lazy val newtype = "io.estatico" %% "newtype"       % "0.4.4"
}
