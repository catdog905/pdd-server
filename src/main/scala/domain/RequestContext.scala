package ru.catdog905
package domain

import cats.data.ReaderT
import cats.effect.IO
import sttp.tapir.Codec
import sttp.tapir.CodecFormat.TextPlain

final case class RequestContext(requestId: String)

object RequestContext {
  implicit val codec: Codec[String, RequestContext, TextPlain] =
    Codec.string.map(RequestContext(_))(_.requestId)

  type ContextualIO[T] = ReaderT[IO, RequestContext, T]
}