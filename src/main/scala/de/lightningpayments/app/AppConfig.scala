package de.lightningpayments.app

import com.typesafe.config.ConfigFactory
import de.commons.lib.spark.SparkSessionLoader
import org.apache.log4j.Logger
import play.api.Configuration

trait AppConfig {

  protected implicit val logger: Logger = Logger.getLogger(this.getClass)

  protected implicit val configuration: Configuration = Configuration(ConfigFactory.load("application.conf"))

  protected implicit val loader: SparkSessionLoader = new SparkSessionLoader(configuration)

  logger.info("spark master: " + configuration.get[String]("spark.master"))

}
