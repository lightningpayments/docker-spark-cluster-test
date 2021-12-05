package de.lightningpayments.app

import akka.http.interop.ZIOSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.Show
import cats.implicits._
import de.lightningpayments.app.errors.DomainError
import de.lightningpayments.app.errors.DomainError.FatalError
import org.apache.log4j.{Logger => Log4jLogger}
import zio.{IO, Task}

trait Routes extends ZIOSupport {

  def getSparkRoute[A: Show](io: => Task[A]): Route =
    path("spark") {
      get {
        complete {
          io.foldM(t => IO.fail(FatalError(t)), a => IO.succeed(a.show)): IO[DomainError, String]
        }
      }
    }

}
