package repository

import cats.effect.IO
import doobie._
import doobie.implicits._
import doobie.hikari.HikariTransactor

import models.Word

class DoobieWordRepository(xa: Transactor[IO]) extends WordRepository:
  def save(word: Word): IO[Int] = {
    val query =
      sql"""
      INSERT INTO words (user_id, german, english)
      VALUES (${word.userId}, ${word.german}, ${word.english})
      """.update.run
    query.transact(xa)
  }

  def getWords(userId: Int): IO[List[Word]] = {
    val query = sql"""
    SELECT id, user_id, german, english FROM words WHERE user_id = $userId
    """.query[Word].to[List]
    query.transact(xa)
  }
