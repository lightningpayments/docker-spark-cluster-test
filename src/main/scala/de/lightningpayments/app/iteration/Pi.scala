package de.lightningpayments.app.iteration

import org.apache.spark.sql.SparkSession
import zio.{Has, ZIO}

object Pi {

  def run(spark: SparkSession): ZIO[Has[RandomNumberEnv], Throwable, Double] =
    for {
      rangeTo    <- ZIO.accessM[Has[RandomNumberEnv]](_.get.randomNIntGen(1000))
      predicates  = spark.sparkContext.parallelize(1 to rangeTo).toLocalIterator.toList.map { _ =>
        for {
          (x, y) <- ZIO.tupled(
            ZIO.accessM[Has[RandomNumberEnv]](_.get.randomMathGen),
            ZIO.accessM[Has[RandomNumberEnv]](_.get.randomMathGen)
          )
          predicate = x * x + y * y < 1
        } yield predicate
      }
      sequenced <- ZIO.collectAll(predicates)
    } yield 4.0 * sequenced.count(identity) / rangeTo

}
