package com.avast.sst.example.service

import doobie.implicits._
import doobie.util.transactor.Transactor
import zio.Task
import zio.interop.catz._

trait RandomService {

  def randomNumber: Task[Double]

}

object RandomService {

  def apply(transactor: Transactor[Task]): RandomService = new RandomService {
    override def randomNumber: Task[Double] = {
      sql"select random()"
        .query[Double]
        .unique
        .transact(transactor)
    }
  }

}
