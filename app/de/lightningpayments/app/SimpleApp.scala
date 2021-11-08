package de.lightningpayments.app

import de.commons.lib.spark.SparkRunnable.SparkRZIO
import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import de.lightningpayments.app.calculate.{Iterations, RandomNumberEnv}
import org.apache.log4j.Logger
import play.api.Configuration
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

final class SimpleApp @Inject()(
    controllerComponents: ControllerComponents,
    configuration: Configuration)(
    implicit val ec: ExecutionContext
) extends AbstractController(controllerComponents) {

  private val runtime: zio.Runtime[zio.ZEnv] = zio.Runtime.default
  private val env = new SparkEnvironment(configuration, Logger.getLogger(this.getClass)) with RandomNumberEnv
  private val program = new SparkRZIO[SparkEnvironment, RandomNumberEnv, Double](Iterations.run).run.provide(env)

  def spark: Action[AnyContent] = Action.async {
    runtime.unsafeRunToFuture(program.either.map {
      case Right(value) => Ok(value.toString)
      case Left(ex)     => InternalServerError(ex.getMessage)
    })
  }
}
