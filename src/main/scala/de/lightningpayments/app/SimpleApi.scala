package de.lightningpayments.app

import akka.actor.ActorSystem
import akka.http.interop.HttpServer
import com.typesafe.config.ConfigFactory
import de.commons.lib.spark.services.Spark
import de.lightningpayments.app.iteration.{Pi, RandomNumberEnv}
import org.apache.log4j.{Logger => Log4jLogger}
import play.api.Configuration
import zio.NeedsEnv.needsEnvAmbiguous1
import zio._

import java.nio.file.Paths

object SimpleApi extends zio.App with Routes {
  self =>

  private val configPath             = Paths.get("/Users/ronnywels/Coding/plugins/spark_test_app/src/main/resources/application.conf")
  private implicit val logger        = Log4jLogger.getRootLogger
  private implicit val configuration = Configuration(ConfigFactory.parseFile(configPath.toFile).resolve())
  private val actorSystem            = ActorSystem("spark-server", configuration.underlying)
  private val host                   = configuration.get[String]("server.host")
  private val port                   = configuration.get[Int]("server.port")
  private val actorSystemLayer       = ZLayer.succeed(actorSystem)
  private val serverConfigLayer      = ZLayer.succeed(HttpServer.Config(host, port))
  private val sparkLayer             = Spark.apply
  private val randomNumberLayer      = ZLayer.succeed(new RandomNumberEnv {})

  private val pi: Task[Double] =
    sparkLayer
      .provideLayer(Spark.live)
      .flatMap(_.sparkM)
      .flatMap(Pi.run)
      .provideLayer(randomNumberLayer)

  private val routesLayer = ZLayer.fromEffect(Task(self.getSparkRoute(pi)))
  private val httpServer  = HttpServer.start.provideLayer(HttpServer.live)
  private val serverLive  = httpServer.provideLayer(actorSystemLayer ++ serverConfigLayer ++ routesLayer).toLayer

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    ZIO(scala.io.StdIn.readLine()).provideLayer(serverLive).exitCode

}
