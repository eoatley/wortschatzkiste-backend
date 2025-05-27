package client

import cats.effect.IO
import org.http4s.Uri
import org.http4s.client.Client
import io.circe._
import io.circe.parser._
import io.circe.generic.auto._
import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup

import java.net.URLEncoder

class WiktionaryClient(client: Client[IO]):

  case class Definition(definition: String)
  case class Entry(partOfSpeech: String, language: String, definitions: List[Definition])
  case class WiktionaryResponse(de: List[Entry])

  def fetch(word: String): IO[String] =
    val encoded = URLEncoder.encode(word, "UTF-8")
    val url = Uri.unsafeFromString(s"https://en.wiktionary.org/api/rest_v1/page/definition/$encoded")
    client.expect[String](url).flatMap { rawJson =>
      decode[WiktionaryResponse](rawJson) match {
        case Right(parsed) =>
          val firstRelevantDef = parsed.de
            .filter(entry => entry.language.toLowerCase.contains("german"))
            .flatMap(_.definitions.headOption.map(_.definition)) // Get the first definition
            .map { rawHtml =>
              val unescaped = StringEscapeUtils.unescapeJava(rawHtml)
              Jsoup.parse(unescaped).text()
            }

          firstRelevantDef match {
            case definition :: definitions => IO.pure(definition)
            case _ => IO.raiseError(new RuntimeException("No German definition found"))
          }

        case Left(error) =>
          IO.raiseError(new RuntimeException(s"JSON decode failed: $error"))
      }
    }
