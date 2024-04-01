package ru.catdog905
package error

import cats.implicits.catsSyntaxOptionId

sealed trait AppError {
  val message: String
  val cause: Option[Throwable] = None
}
final case class InternalError(
  cause0: Throwable
) extends AppError {
  override val message: String = "Internal error"
  override val cause: Option[Throwable] = cause0.some
}

case object CanNotCreateUser extends AppError {
  override val message: String = "Can not create user"
}

case object NotSuchUserExists extends AppError {
  override val message: String = "NoSuchUserExists"
}
