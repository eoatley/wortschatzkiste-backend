package routes

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import models.User
import services.UserService

object UserRoutes:
  def routes(service: UserService): HttpRoutes[IO] =
    given EntityDecoder[IO, User] = jsonOf[IO, User]

    HttpRoutes.of[IO]:
      case req @ POST -> Root / "users" =>
        for
          user <- req.as[User]
          _    <- service.createUser(user)
          res  <- Created()
        yield res
      case req@POST -> Root / "login" =>
        for {
          user <- req.as[User]
          existingUser <- service.findUserByUsername(user.username)
          res <- existingUser match {
            case Some(u: User) =>
              Ok(s"Logged in successfully as ${u.username} with id ${u.id.getOrElse("unknown")}. Use this id as your token for future requests.")
            case None =>
              Forbidden("User not found")
          }
        } yield res
