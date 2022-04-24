package ru

import io.circe.Encoder
import io.estatico.newtype.macros.newtype
import io.estatico.newtype.ops._

package object kor85 {

  @newtype case class Title(value: String)
  object Title {
    implicit val encoder: Encoder[Title] = implicitly[Encoder[String]].coerce
  }
}
