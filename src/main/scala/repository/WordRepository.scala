package repository

import cats.effect.IO
import models.Word

trait WordRepository:
  def save(word: Word): IO[Int]
  def getWords(userId: Int): IO[List[Word]]
