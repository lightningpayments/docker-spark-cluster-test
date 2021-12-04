package de.lightningpayments.app

import de.commons.lib.spark.services.Spark
import de.lightningpayments.app.iteration.Iterations
import de.lightningpayments.app.server.HttpServer
import zio.NeedsEnv.needsEnvAmbiguous1
import zio._

object SimpleApp extends zio.App with RoutesAppSupport with ApiConfig { self =>

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val routesLayer       = ZLayer.succeed(self.getSparkRoute(program))
    val allLayers         = actorSystemLayer ++ serverConfigLayer ++ routesLayer ++ HttpServer.live
    val httpServerManaged = HttpServer.start.provideLayer(allLayers)
    val httpServerLayer   = ZLayer.fromManaged(httpServerManaged)
    ZIO.never.provideLayer(httpServerLayer)
  }

  private val program: Task[Double] =
    sparkLayer
      .provideLayer(Spark.live)
      .>>=(_.sparkM)
      .>>=(Iterations.run)
      .provideLayer(randomNumberLayer)


}
