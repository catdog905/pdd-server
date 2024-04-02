package ru.catdog905
package domain

import derevo.derive
import derevo.circe.{decoder, encoder}
import io.circe.{Decoder, Encoder}
import io.circe.derivation.{deriveDecoder, deriveEncoder}
import sttp.tapir.Schema
import sttp.tapir.derevo.schema

@derive(encoder, decoder, schema)
final case class Replenishment(
                        ticketId: String,
                        userName: UserName,
                        reward: Int
                        )

object Replenishment {
//  implicit val replenishmentEncoder: Encoder[Replenishment] = deriveEncoder[Replenishment]
//  implicit val replenishmentDecoder: Decoder[Replenishment] = deriveDecoder[Replenishment]
//  implicit val replenishmentSchema: Schema[Replenishment] = Schema.derived
}
