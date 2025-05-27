package services

import cats.effect.*
import client.WiktionaryClient
import models.Word
import repository.DoobieWordRepository


class WordService(client: WiktionaryClient, repository: DoobieWordRepository) {

  def translate(german: String): IO[String] = {
    client.fetch(german)
  }

  def saveWord(word: Word): IO[Int] = {
    repository.save(word)
  }

  def getWords(userId: Int): IO[List[Word]] = {
    repository.getWords(userId)
  }
}
