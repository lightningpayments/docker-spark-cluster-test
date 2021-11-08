package de.lightningpayments.app

import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

final class SimpleApp @Inject()(
    controllerComponents: ControllerComponents,
    sparkService: SparkService)(
    implicit val ec: ExecutionContext
)
  extends AbstractController(controllerComponents) {

  private val runtime: zio.Runtime[zio.ZEnv] = zio.Runtime.global

  def spark: Action[AnyContent] = Action.async {
    runtime.unsafeRunToFuture(sparkService.run.either.map {
      case Right(value) => Ok(value.toString)
      case Left(ex)     => InternalServerError(ex.getMessage)
    })
  }
}
