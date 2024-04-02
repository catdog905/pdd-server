package ru.catdog905
package domain

import derevo.circe.{decoder, encoder}
import derevo.derive
import sttp.tapir.derevo.schema

@derive(encoder, decoder, schema)
final case class UserName(gitlab_name: String)

@derive(encoder, decoder)
final case class User(name: UserName)