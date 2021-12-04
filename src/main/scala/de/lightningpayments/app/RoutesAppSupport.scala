package de.lightningpayments.app

import cats.implicits._
import akka.http.interop.ZIOSupport
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import cats.Show
import de.lightningpayments.app.errors.DomainError
import de.lightningpayments.app.errors.DomainError.FatalError
import zio.{IO, Task}

trait RoutesAppSupport extends ZIOSupport {

  def getSparkRoute[A: Show](program: => Task[A]): Route =
    path("spark") {
      get {
        complete {
          program.map(_.show).foldM(t => IO.fail(FatalError(t)), IO.succeed(_)): IO[DomainError, String]
        }
      }
    }

}
