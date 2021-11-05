package de.lightningpayments.app

import akka.actor.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}
import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import org.apache.log4j.Logger
import play.api.Configuration

// $COVERAGE-OFF$
trait AppConfig {

  protected val runtime: zio.Runtime[zio.ZEnv] = zio.Runtime.global

  private val config: Config = ConfigFactory.load("application.conf")

  protected implicit val system: ActorSystem = ActorSystem("simple-app-http-server", config)

  protected val logger: Logger = Logger.getLogger(this.getClass)

  protected val configuration: Configuration = Configuration(config)

  protected val env = new SparkEnvironment(configuration, logger)

  println("spark master: " + configuration.get[String]("spark.master"))

}
// $COVERAGE-ON$