package de.lightningpayments.app

import akka.http.interop.HttpServer
import de.commons.lib.spark.services.Spark
import de.lightningpayments.app.iteration.Iterations
import zio.NeedsEnv.needsEnvAmbiguous1
import zio._

object SimpleApp extends zio.App with RoutesAppSupport with ApiConfig { self =>

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val routesLayer       = ZLayer.succeed(self.getSparkRoute(program))
    val allLayers         = actorSystemLayer ++ serverConfigLayer ++ routesLayer
    val httpServerManaged = HttpServer.start.provideLayer(HttpServer.live)
    val managed           = httpServerManaged.provideLayer(allLayers)

    ZIO.unit.fork.provideLayer(managed.toLayer).exitCode
  }

  private val program: Task[Double] =
    sparkLayer
      .provideLayer(Spark.live)
      .>>=(_.sparkM)
      .>>=(Iterations.run)
      .provideLayer(randomNumberLayer)


}
