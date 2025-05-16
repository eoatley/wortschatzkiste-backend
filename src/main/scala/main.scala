import cats.effect._
import org.http4s.blaze.server.BlazeServerBuilder
import routes.UserRoutes
import doobie._
import doobie.hikari.HikariTransactor
import scala.concurrent.ExecutionContext

object Main extends IOApp:

  def createTransactor: Resource[IO, HikariTransactor[IO]] =
    for
      ce <- Resource.eval(IO.executionContext)
      xa <- HikariTransactor.newHikariTransactor[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost:5432/wortschatzkiste",
        "postgres",
        "postgres",
        ce
      )
    yield xa


  override def run(args: List[String]): IO[ExitCode] =
    createTransactor.use { xa =>
      val userService = new service.UserService(xa)
      val routes = UserRoutes.routes(userService)

      BlazeServerBuilder[IO]
        .bindHttp(8080, "localhost")
        .withHttpApp(org.http4s.server.Router("/" -> routes).orNotFound)
        .resource
        .use(_ => IO.never)
        .as(ExitCode.Success)
    }
