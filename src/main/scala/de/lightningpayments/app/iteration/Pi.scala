package de.lightningpayments.app.iteration

import org.apache.spark.sql.SparkSession
import zio.{Has, ZIO}

object Pi {

  def run(spark: SparkSession): ZIO[Has[RandomNumberEnv], Throwable, Double] = {
    println("PI")
    ZIO.accessM[Has[RandomNumberEnv]](_.get.randomNIntGen(1000)).flatMap { to =>
      val randomNumber = ZIO.accessM[Has[RandomNumberEnv]](_.get.randomMathGen)

      ZIO
        .collectAll {
          spark.sparkContext.parallelize(1 to to).toLocalIterator.toList.map { _ =>
            ZIO.tupled(randomNumber, randomNumber).map {
              case (x, y) => x * x + y * y < 1
            }
          }
        }
        .map(seq => 4.0 * seq.count(identity) / to)
    }
  }

}
