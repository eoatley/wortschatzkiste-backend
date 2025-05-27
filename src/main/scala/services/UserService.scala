package services

import cats.effect.IO
import models.User
import repository.UserRepository

class UserService(userRepo: UserRepository):

  def createUser(user: User): IO[Int] =
    userRepo.saveUser(user.username).attempt.flatMap {
      case Left(e) =>
        IO.println(s"[DB ERROR] Failed to insert user: ${e.getMessage}") *> IO.raiseError(e)
      case Right(id) =>
        IO.println(s"[DB SUCCESS] Inserted user: ${user.username} with id: $id") *> IO.pure(id)
    }

  def findUserByUsername(username: String): IO[Option[User]] =
    userRepo.getUser(username).attempt.flatMap {
      case Left(e) =>
        IO.println(s"[DB ERROR] Failed to find user: ${e.getMessage}") *> IO.raiseError(e)
      case Right(userOpt) =>
        userOpt match {
          case Some(user) =>
            IO.println(s"[DB SUCCESS] Found user: ${user.username} with id: ${user.id.getOrElse("unknown")}") *> IO.pure(Some(user))
          case None =>
            IO.println(s"[DB SUCCESS] No user found with username: $username") *> IO.pure(None)
        }
    }
