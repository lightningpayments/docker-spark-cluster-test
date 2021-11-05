package de.lightningpayments.app

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.commons.lib.spark.SparkRunnable.SparkRZIO
import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import de.lightningpayments.app.calculate.Iterations
import zio._

import scala.concurrent.Future

// $COVERAGE-OFF$
object SimpleApp extends zio.App with AppConfig {

  private val route: IO[Throwable, Unit] => Route = io =>
    path("/") {
      get {
        onSuccess(runtime.unsafeRunToFuture(io))(complete(StatusCodes.Created))
      }
    }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Task.fromFuture { implicit ec =>
      Future {
        Http()
          .newServerAt("10.5.0.6", 9000) // ip address from container
          .bind(route(new SparkRZIO[SparkEnvironment, Any, Unit](io = Iterations.run.map(_.show)).run.provide(env)))
          .flatMap(_.unbind())
          .onComplete(_ => system.terminate())
      }
    }.exitCode
}
// $COVERAGE-ON$
