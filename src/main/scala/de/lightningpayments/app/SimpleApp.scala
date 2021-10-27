package de.lightningpayments.app

import de.commons.lib.spark.SparkRunnable.SparkRZIO
import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import de.lightningpayments.app.calculate.PiIterations
import zio.{ExitCode, URIO}

object SimpleApp extends zio.App with AppConfig {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {

    new SparkRZIO[SparkEnvironment, Any, Unit](io = PiIterations.run.map(_.show))
      .run
      .provide(new SparkEnvironment(configuration, logger))
      .exitCode
  }

}
