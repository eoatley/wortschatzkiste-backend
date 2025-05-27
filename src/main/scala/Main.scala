import cats.effect.*
import cats.syntax.apply.* // for .tupled
import client.WiktionaryClient
import com.comcast.ip4s.{ipv4, port}
import com.zaxxer.hikari.HikariConfig
import doobie.hikari.HikariTransactor
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import repository.{DoobieUserRepository, DoobieWordRepository}
import routes.{UserRoutes, WordRoutes}
import services.{UserService, WordService}

object Main extends IOApp:

  def run(args: List[String]): IO[ExitCode] =
    val clientResource = EmberClientBuilder.default[IO].build
    val transactorResource = for {
      hikariConfig <- Resource.eval(IO {
        val config = new HikariConfig()
        config.setDriverClassName("org.postgresql.Driver")
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/wortschatzkiste")
        config.setUsername("postgres")
        config.setPassword("postgres")
        config
      })
      xa <- HikariTransactor.fromHikariConfig[IO](hikariConfig)
    } yield xa

    (clientResource, transactorResource).tupled.use { (client, xa) =>
      val userRepo = new DoobieUserRepository(xa)
      val wordRepo = new DoobieWordRepository(xa)
      val userService = new UserService(userRepo)
      val wordService = new WordService(new WiktionaryClient(client), wordRepo)
      val userRoutes = UserRoutes.routes(userService)
      val wordRoutes = WordRoutes.routes(wordService)

      EmberServerBuilder
        .default[IO]
        .withHost(ipv4"127.0.0.1")
        .withPort(port"8080")
        .withHttpApp(Router(
          "/users" -> userRoutes,
          "/words" -> wordRoutes
        ).orNotFound)
        .build
        .use(_ => IO.never)
        .as(ExitCode.Success)
    }
