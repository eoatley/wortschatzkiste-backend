package repository

import cats.effect.IO
import models.User

trait UserRepository:
  def saveUser(username: String): IO[Int]
  def getUser(username: String): IO[Option[User]]
