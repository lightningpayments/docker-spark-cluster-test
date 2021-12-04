package de.lightningpayments.app

import akka.http.interop.ZIOSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.Show
import cats.implicits._
import de.lightningpayments.app.errors.DomainError
import de.lightningpayments.app.errors.DomainError.FatalError
import zio.{IO, Task}

trait Routes extends ZIOSupport {

  def getSparkRoute[A](program: => Task[A])(implicit show: Show[A]): Route =
    path("spark") {
      get {
        complete {
          program.map(_.show).foldM(t => IO.fail(FatalError(t)), IO.succeed(_)): IO[DomainError, String]
        }
      }
    }

}
