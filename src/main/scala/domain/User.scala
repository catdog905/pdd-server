package ru.catdog905
package domain

final case class UserName(gitlab_name: String)

final case class User(name: UserName)