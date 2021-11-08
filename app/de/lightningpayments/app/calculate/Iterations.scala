package de.lightningpayments.app.calculate

import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import zio.ZIO

object Iterations {

  val run: ZIO[SparkEnvironment, Throwable, Double] =
    ZIO.environment[SparkEnvironment].flatMap(_.sparkM).map { spark =>
      spark.sparkContext.parallelize(1 to 100).filter { _ =>
        val x = math.random
        val y = math.random
        x * x + y * y < 1
      }.count()
    }.map(count => 4.0 * count / 100)

}
