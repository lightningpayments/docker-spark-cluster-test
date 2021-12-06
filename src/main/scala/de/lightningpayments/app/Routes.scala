package de.lightningpayments.app

import akka.http.interop.ZIOSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.Show
import cats.implicits._
import akka.http.scaladsl.model.StatusCodes
import zio.{Task, IO}

trait Routes {

  private val runtime = zio.Runtime.default

  def getSparkRoute[A: Show](io: => Task[A]): Route =
    path("spark") {
      get {
        onSuccess(runtime.unsafeRunToFuture(io.fold(
          _ => complete(StatusCodes.InternalServerError),
          d => complete(d.show)
        )))(identity)
      }
    }

}
