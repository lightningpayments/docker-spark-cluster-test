package de.lightningpayments.app.calculate

import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import org.apache.spark.sql.{Dataset, Encoder, Encoders}
import zio.ZIO

object Iterations {

  final case class Dummy(value: Int) extends AnyVal

  object Dummy {
    implicit val encoder: Encoder[Dummy] = Encoders.product[Dummy]
  }

  val run: ZIO[SparkEnvironment, Throwable, Dataset[Dummy]] =
    ZIO.environment[SparkEnvironment].flatMap(_.sparkM).map { spark =>
      import Dummy._
      import spark.implicits._
      spark.sparkContext.parallelize(1 to 10).toDS.as[Dummy]
    }

}
