package ru.kor85

import _root_.io.circe.Encoder
import _root_.io.circe.generic.auto._
import _root_.io.circe.syntax._
import cats.data.EitherT
import cats.effect._
import cats.syntax.flatMap._
import cats.syntax.functor._
import fs2._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

import java.net.URL
import scala.util.Try

class Routes[F[_]](crawler: Crawler[F])(implicit F: Async[F]) extends Http4sDsl[F] {
  import ru.kor85.Routes._

  implicit private val reqDecoder: EntityDecoder[F, TitlesRequest] =
    jsonOf[F, TitlesRequest]

  implicit private val respEntityEncoder: EntityEncoder[F, TitlesResponseItem] =
    jsonEncoderOf[F, TitlesResponseItem]

  implicit private val respStreamEncoder: EntityEncoder[F, Stream[F, TitlesResponseItem]] =
    EntityEncoder
      .streamEncoder[F, String]
      .contramap[Stream[F, TitlesResponseItem]](stream =>
        Stream.emit("[") ++ stream.map(_.asJson.noSpaces).intersperse(",") ++ Stream.emit("]")
      )

  val titles: HttpRoutes[F] = HttpRoutes.of {
    case req @ POST -> Root / "titles" =>
      req
        .as[TitlesRequest]
        .flatMap(req => Ok(titleStream(req.urls)))
  }

  private def titleStream(urls: Seq[String]): Stream[F, TitlesResponseItem] = {
    def getTitle(rawUrl: String): F[TitlesResponseItem] =
      EitherT(F.delay(Try(new URL(rawUrl)).toEither))
        .flatMapF(crawler.crawlTitle)
        .value
        .map(TitlesResponseItem(rawUrl, _))

    Stream
      .emits(urls)
      .parEvalMapUnordered[F, TitlesResponseItem](2 * numCores)(getTitle)
  }
}

object Routes {

  private val numCores = Runtime.getRuntime.availableProcessors()

  case class TitlesRequest(urls: Vector[String])

  case class TitlesResponseItem(url: String, result: Either[Throwable, Title])

  implicit private val throwableEncoder: Encoder[Throwable] =
    Encoder.encodeString.contramap(_.getMessage)

  implicit private val eitherEncoder: Encoder[Either[Throwable, Title]] =
    Encoder.encodeEither("error", "title")
}
