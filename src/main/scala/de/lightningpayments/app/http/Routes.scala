package de.lightningpayments.app.http

import akka.http.interop.ZIOSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.lightningpayments.app.errors.DomainError
import de.lightningpayments.app.errors.DomainError.{FatalError, _}
import zio.{IO, Task}

trait Routes extends ZIOSupport {

  def getSparkRoute[A](io: => Task[A]): Route =
    path("spark") {
      get {
        complete {
          io
            .map(d => s"$d")
            .flatMapError[Any, DomainError](t => IO.fail(FatalError(t)))
        }
      }
    }

}
