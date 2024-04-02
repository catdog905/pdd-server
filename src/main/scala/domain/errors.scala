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

  object AppError {
    //  implicit val throwableEncoder: Encoder[Throwable] = Encoder.encodeString.contramap(_.toString)
    //  implicit val throwableDecoder: Decoder[Throwable] = Decoder.decodeString.emap { str =>
    //    Right(new Throwable(str))
    //  }
    //
    //  implicit val appErrorEncoder: Encoder[AppError] = Encoder.instance {
    //    case internalError: InternalError => deriveEncoder[InternalError].apply(internalError)
    //    case CanNotCreateUser => deriveEncoder[CanNotCreateUser.type].apply(CanNotCreateUser)
    //    case NotSuchUserExists => deriveEncoder[NotSuchUserExists.type].apply(NotSuchUserExists)
    //  }
    //
    //  implicit val internalErrorDecoder: Decoder[InternalError] = Decoder.forProduct1("cause")(InternalError.apply)
    //  implicit val canNotCreateUserDecoder: Decoder[CanNotCreateUser.type] = Decoder.decodeUnit.as(CanNotCreateUser)
    //  implicit val notSuchUserExistsDecoder: Decoder[NotSuchUserExists.type] = Decoder.decodeUnit.as(NotSuchUserExists)
    //
    //  implicit val appErrorDecoder: Decoder[AppError] = new Decoder[AppError] {
    //    final def apply(cursor: HCursor): Decoder.Result[AppError] = {
    //      cursor.downField("message").as[String].flatMap {
    //        case "Internal error" => cursor.as[InternalError]
    //        case "Can not create user" => Right(CanNotCreateUser)
    //        case "NoSuchUserExists" => Right(NotSuchUserExists)
    //        case other => Left(DecodingFailure(s"Unknown error message: $other", cursor.history))
    //      }
    //    }
    //  }
    //
    //
    //  implicit val throwableSchema: Schema[Throwable] = Schema.string
    //
    //  implicit val appErrorSchema: Schema[AppError] = Schema.derived[AppError]
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



  //  implicit val encoder: Encoder[AppError] = (a: AppError) =>
  //    Json.obj(
  //      ("message", Json.fromString(a.message))
  //    )
  //
  //  implicit val decoder: Decoder[AppError] = (c: HCursor) => c.downField("message").as[String].map(DecodedError.apply)

  implicit val throwableEncoder: Encoder[Throwable] =
    Encoder.encodeString.contramap(_.getMessage)
  implicit val throwableDecoder: Decoder[Throwable] =
    Decoder.decodeString.map(new Throwable(_))
  implicit val schema: Schema[AppError] =
    Schema.schemaForString.map[AppError](str => Some(DecodedError(str)))(
      _.message
    )
}