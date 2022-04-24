package ru.kor85

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val routes = new Routes[IO](Crawler[IO]())

    BlazeServerBuilder[IO]
      .withHttpApp(routes.titles.orNotFound)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
