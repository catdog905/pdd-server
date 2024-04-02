package ru.catdog905
package domain

import cats.implicits.catsSyntaxOptionId
import derevo.circe.{decoder, encoder}
import derevo.derive
import io.circe.{Decoder, Encoder}
import sttp.tapir.Schema


object errors {
  @derive(encoder, decoder)
  sealed trait AppError {
    val message: String
    val cause: Option[Throwable] = None
  }

  @derive(encoder, decoder)
  final case class InternalError(
                                  cause0: Throwable
                                ) extends AppError {
    override val message: String = "Internal error"
    override val cause: Option[Throwable] = cause0.some
  }

  @derive(encoder, decoder)
  case object CanNotCreateUser extends AppError {
    override val message: String = "Can not create user"
  }

  @derive(encoder, decoder)
  case object NotSuchUserExists extends AppError {
    override val message: String = "NoSuchUserExists"
  }

  @derive(encoder, decoder)
  case class DecodedError(override val message: String)
    extends AppError


  implicit val throwableEncoder: Encoder[Throwable] =
    Encoder.encodeString.contramap(_.getMessage)
  implicit val throwableDecoder: Decoder[Throwable] =
    Decoder.decodeString.map(new Throwable(_))
  implicit val schema: Schema[AppError] =
    Schema.schemaForString.map[AppError](str => Some(DecodedError(str)))(
      _.message
    )
}