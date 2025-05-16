package routes

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import model.User
import service.UserService

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
