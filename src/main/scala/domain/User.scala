package ru.catdog905
package domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import io.circe.{Decoder, Encoder}
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import sttp.tapir.Schema
import sttp.tapir.derevo.schema

@derive(encoder, decoder, schema)
final case class UserName(gitlab_name: String)

object UserName{
//  implicit val userNameEncoder: Encoder[UserName] = deriveEncoder[UserName]
//  implicit val userNameDecoder: Decoder[UserName] = deriveDecoder[UserName]
//  implicit val replenishmentSchema: Schema[UserName] = Schema.derived
}

@derive(encoder, decoder)
final case class User(name: UserName)