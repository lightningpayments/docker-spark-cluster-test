package de.lightningpayments.app

import akka.actor.ActorSystem
import akka.http.interop.HttpServer
import com.typesafe.config.ConfigFactory
import de.commons.lib.spark.services.Spark
import de.lightningpayments.app.iteration.RandomNumberEnv
import org.apache.log4j.{Logger => Log4jLogger}
import play.api.Configuration
import zio.ZLayer

trait ApiConfig {
  self =>

  protected implicit val logger: Log4jLogger = Log4jLogger.getLogger(this.getClass)

  protected implicit val configuration = Configuration(ConfigFactory.load("/application.conf"))
  protected val actorSystem = ActorSystem("spark-server", configuration.underlying)

  protected val host = configuration.get[String]("server.host")
  protected val port = configuration.get[Int]("server.port")

  protected val actorSystemLayer  = ZLayer.succeed(actorSystem)
  protected val serverConfigLayer = ZLayer.succeed(HttpServer.Config(host, port))
  protected val sparkLayer        = Spark.apply
  protected val randomNumberLayer = ZLayer.succeed(new RandomNumberEnv {})

}
