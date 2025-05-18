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

  def findUserByUsername(username: String): IO[Option[User]] =
    sql"SELECT id, username FROM users WHERE username = $username"
      .query[User]
      .option
      .transact(xa)
      .attempt.flatMap {
        case Left(e) =>
          IO.println(s"[DB ERROR] Failed to find user: ${e.getMessage}") *> IO.raiseError(e)
        case Right(userOpt) =>
          userOpt match {
            case Some(user) =>
              IO.println(s"[DB SUCCESS] Found user: ${user.username} with id: ${user.id.getOrElse("unknown")}") *> IO.pure(userOpt)
            case None =>
              IO.println(s"[DB SUCCESS] No user found with username: $username") *> IO.pure(userOpt)
          }
      }
