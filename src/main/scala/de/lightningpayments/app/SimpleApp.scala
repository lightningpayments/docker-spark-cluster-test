package de.lightningpayments.app

import akka.http.interop.ZIOSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.commons.lib.spark.services.Spark
import de.lightningpayments.app.errors.DomainError
import de.lightningpayments.app.errors.DomainError._
import de.lightningpayments.app.iteration.Iterations
import zio.{IO, _}

object SimpleApp extends zio.App with ZIOSupport with ApiConfig {

  private val program: Task[Double] =
    sparkLayer
      .provideLayer(Spark.live).>>=(_.sparkM).>>=(Iterations.run)
      .provideLayer(randomNumberLayer)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val routes: Route =
      path("spark") {
        get {
          complete {
            program
              .map(d => s"$d")
              .flatMapError[Any, DomainError](t => IO.fail(FatalError(t)))
          }
        }
      }


    ???
  }


}
