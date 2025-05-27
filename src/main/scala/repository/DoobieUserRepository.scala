package repository

import cats.effect.IO
import doobie._
import doobie.implicits._
import doobie.hikari.HikariTransactor
import models.User
import repository.UserRepository

class DoobieUserRepository(xa: HikariTransactor[IO]) extends UserRepository:

  override def saveUser(username: String): IO[Int] =
    sql"INSERT INTO users (username) VALUES ($username) RETURNING id"
      .query[Int]
      .unique
      .transact(xa)

  override def getUser(username: String): IO[Option[User]] =
    sql"SELECT id, username FROM users WHERE username = $username"
      .query[User]
      .option
      .transact(xa)
