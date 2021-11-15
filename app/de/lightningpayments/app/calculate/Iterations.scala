package de.lightningpayments.app.calculate

import de.commons.lib.spark.environments.SparkR
import zio.ZIO

object Iterations {

  val run: ZIO[SparkR with RandomNumberEnv, Throwable, Double] =
    for {
      env        <- ZIO.environment[SparkR with RandomNumberEnv]
      spark      <- env.sparkM
      rangeTo    <- env.randomNIntGen(1000)
      predicates  = spark.sparkContext.parallelize(1 to rangeTo).toLocalIterator.toList.map { _ =>
        for {
          (x, y)    <- ZIO.tupled(env.randomMathGen, env.randomMathGen)
          predicate  = x * x + y * y < 1
        } yield predicate
      }
      sequenced  <- ZIO.collectAll(predicates)
    } yield 4.0 * sequenced.count(identity) / rangeTo

}
