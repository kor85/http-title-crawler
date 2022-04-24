package ru.kor85

import cats.effect.Sync
import net.ruippeixotog.scalascraper.browser.JsoupBrowser

import java.net.URL
import scala.util.Try

trait Crawler[F[_]] {
  def crawlTitle(url: URL): F[Either[Throwable, Title]]
}

object Crawler {

  def apply[F[_]]()(implicit F: Sync[F]): Crawler[F] =
    new Crawler[F] {
      private val browser = JsoupBrowser()

      override def crawlTitle(url: URL): F[Either[Throwable, Title]] =
        F.delay {
          Try {
            Title(browser.get(url.toString).title)
          }.toEither
        }
    }
}
