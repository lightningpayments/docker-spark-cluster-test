package de.lightningpayments.app

import akka.http.interop.HttpServer
import de.commons.lib.spark.services.Spark
import de.lightningpayments.app.iteration.Pi
import zio.NeedsEnv.needsEnvAmbiguous1
import zio._

object SimpleApp extends zio.App with ApiConfig with Routes {
  self =>

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val routesLayer = ZLayer.succeed(self.getSparkRoute(program))
    val httpServer  = HttpServer.start.provideLayer(HttpServer.live)
    val serverLive  = httpServer.provideLayer(actorSystemLayer ++ serverConfigLayer ++ routesLayer).toLayer

    ZIO.unit.provideLayer(serverLive).forkDaemon.exitCode
  }

  private val program: Task[Double] =
    sparkLayer
      .provideLayer(Spark.live)
      .>>=(_.sparkM)
      .>>=(Pi.run)
      .provideLayer(randomNumberLayer)

}
