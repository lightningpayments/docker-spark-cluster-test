package de.lightningpayments.app.calculate

import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import de.lightningpayments.{SparkTestSupport, TestSpec}

class IterationsSpec extends TestSpec with SparkTestSupport {

  "Iterations#run" must {
    "return a set of iterations as count" in withSparkSession { _ => logger =>
      val program = Iterations.run
      whenReady(Iterations.run.provide(new SparkEnvironment(configuration, logger)))(_.isRight mustBe true)
    }
  }

}
