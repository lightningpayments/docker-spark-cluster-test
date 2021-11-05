package de.lightningpayments.app

import de.commons.lib.spark.SparkRunnable.SparkRZIO
import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import de.lightningpayments.app.calculate.Iterations
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SimpleApp @Inject()(
    controllerComponents: ControllerComponents)(
    implicit val ec: ExecutionContext
)
  extends AbstractController(controllerComponents)
  with AppConfig {

  private val io = new SparkRZIO[SparkEnvironment, Any, Unit](io = Iterations.run.map(_.show)).run.provide(env)

  def spark: Action[AnyContent] = Action.async {
    for {
      _      <- runtime.unsafeRunToFuture(io)
      result <- Future.successful(NoContent)
    } yield result
  }
}
