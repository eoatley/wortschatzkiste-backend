package services

import cats.effect._
import doobie._
import doobie.implicits._
import models.User

class UserService(xa: Transactor[IO]):
  def createUser(user: User): IO[Unit] =
    sql"INSERT INTO users (username) VALUES (${user.username})"
      .update.run
      .transact(xa)
      .attempt.flatMap {
        case Left(e) =>
          IO.println(s"[DB ERROR] Failed to insert user: ${e.getMessage}") *> IO.raiseError(e)
        case Right(_) =>
          IO.println(s"[DB SUCCESS] Inserted user: ${user.username}")
      }
