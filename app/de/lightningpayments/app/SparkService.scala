package de.lightningpayments.app

import de.commons.lib.spark.SparkRunnable.SparkRZIO
import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import de.lightningpayments.app.calculate.Iterations
import org.apache.log4j.Logger
import play.api.Configuration
import zio.Task

import javax.inject.Inject

final class SparkService @Inject()(configuration: Configuration) {

  private val env = new SparkEnvironment(configuration, Logger.getLogger(this.getClass))

  val run: Task[Double] = new SparkRZIO[SparkEnvironment, Any, Double](Iterations.run).run.provide(env)

}
