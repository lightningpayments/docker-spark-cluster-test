package de.lightningpayments.app

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import de.commons.lib.spark.services.Spark
import de.lightningpayments.app.SimpleApp.program
import de.lightningpayments.app.iteration.RandomNumberEnv
import de.lightningpayments.app.server.HttpServer
import org.apache.log4j.{Logger => Log4jLogger}
import play.api.Configuration
import zio.ZLayer

import java.nio.file.Paths

trait ApiConfig {
  self =>

  protected implicit val logger: Log4jLogger = Log4jLogger.getLogger(this.getClass)


  protected val configPath = Paths.get(self.getClass.getResource("/application.conf").getPath)

  protected implicit val configuration = Configuration(ConfigFactory.load(configPath.normalize().toString))
  protected val actorSystem = ActorSystem("spark-server", configuration.underlying)

  protected val host = configuration.get[String]("server.host")
  protected val port = configuration.get[Int]("server.port")

  protected val actorSystemLayer = ZLayer.succeed(actorSystem)
  protected val sparkLayer = Spark.apply
  protected val randomNumberLayer = ZLayer.succeed(new RandomNumberEnv {})
  protected val serverConfigLayer = ZLayer.succeed(HttpServer.Config(host, port))

}
