package de.lightningpayments

import com.typesafe.config.ConfigFactory
import org.apache.log4j.{LogManager, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import play.api.Configuration

import java.util.UUID

trait SparkTestSupport {

  protected implicit val logger: Logger = LogManager.getLogger(this.getClass)

  protected implicit val configuration: Configuration = Configuration(ConfigFactory.parseString(
    """
      |spark {
      |  config {}
      |}
      |host = localhost
      |akka {}
      |""".stripMargin))

  protected val appName: String = s"app_${UUID.randomUUID().toString}"
  protected val master: String = "local[*]"
  protected val sparkConf: Map[String, String] = configuration.get[Map[String, String]]("spark.config")

  private lazy val spark: SparkSession = {
    val config = new SparkConf().setAll(sparkConf)
    val builder = SparkSession.builder().appName(appName).master(master).config(config)
    builder.getOrCreate()
  }

  def withSparkSession[A, T](f: SparkSession => Logger => T): T = f(spark)(logger)
}
