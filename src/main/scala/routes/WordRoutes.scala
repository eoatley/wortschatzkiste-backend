package routes

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.circe._
import io.circe.syntax._
import services.WordService

object WordRoutes {
  def routes(service: WordService): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case req
        @GET -> Root /
        "translate"
      =>
        req.params.get("german") match {
          case Some(german) =>
            for {
              english <- service.translate(german)
              res <- Ok(english.asJson)
            } yield res
          case None =>
            BadRequest("Missing query parameter: german")
        }
    }

}
