package de.lightningpayments.app

import de.commons.lib.spark.services.Spark
import de.lightningpayments.app.http.Routes
import de.lightningpayments.app.iteration.Iterations
import de.lightningpayments.app.server.HttpServer
import zio.NeedsEnv.needsEnvAmbiguous1
import zio._

object SimpleApp extends zio.App with Routes with ApiConfig { self =>

  private val program: Task[Double] =
    sparkLayer
      .provideLayer(Spark.live)
      .>>=(_.sparkM)
      .>>=(Iterations.run)
      .provideLayer(randomNumberLayer)

  private val routesLayer = ZLayer.succeed(self.getSparkRoute(program))
  private val allLayers   = actorSystemLayer ++ serverConfigLayer ++ routesLayer ++ HttpServer.live

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val httpServerManaged = HttpServer.start.provideLayer(allLayers)
    val httpServerLayer = ZLayer.fromManaged(httpServerManaged)
    ZIO.never.provideLayer(httpServerLayer)
  }


}
