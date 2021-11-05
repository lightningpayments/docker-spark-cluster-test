package de.lightningpayments.app

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import de.commons.lib.spark.SparkRunnable.SparkRZIO
import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import de.lightningpayments.app.calculate.Iterations

import scala.io.StdIn
import scala.util.{Failure, Success}

// $COVERAGE-OFF$
object SimpleApp extends AppConfig {

  private val host: String = configuration.get[String]("host")

  private val io = new SparkRZIO[SparkEnvironment, Any, Unit](io = Iterations.run.map(_.show)).run

  def main(args: Array[String]): Unit = {
    import system.dispatcher

    val future = Http()
      .newServerAt(host, 9000) // ip address from container
      .bind {
        path("spark") {
          println("FUCK")
          onComplete(runtime.unsafeRunToFuture(io.provide(env))) {
            case Success(_) => complete(StatusCodes.Created)
            case Failure(_) => complete(StatusCodes.InternalServerError)
          }
        }
      }

    println("Server now online.")

    StdIn.readLine()

    future.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}
// $COVERAGE-ON$
