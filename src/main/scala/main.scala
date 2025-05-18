import cats.effect._
import org.http4s.blaze.server.BlazeServerBuilder
import routes.UserRoutes
import doobie._
import doobie.hikari.HikariTransactor
import com.zaxxer.hikari.HikariConfig

object Main extends IOApp:

  val transactor: Resource[IO, HikariTransactor[IO]] =
    for {
      hikariConfig <- Resource.pure {
        val config = new HikariConfig()
        config.setDriverClassName("org.postgresql.Driver")
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/wortschatzkiste")
        config.setUsername("postgres")
        config.setPassword("postgres")
        config
      }
      xa <- HikariTransactor.fromHikariConfig[IO](hikariConfig)
    } yield xa


  def run(args: List[String]): IO[ExitCode] =
    transactor.use { xa =>
      val userService = new services.UserService(xa)
      val routes = UserRoutes.routes(userService)

      BlazeServerBuilder[IO]
        .bindHttp(8080, "localhost")
        .withHttpApp(org.http4s.server.Router("/" -> routes).orNotFound)
        .resource
        .use(_ => IO.never)
        .as(ExitCode.Success)
    }
