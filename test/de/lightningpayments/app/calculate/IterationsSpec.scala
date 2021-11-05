package de.lightningpayments.app.calculate

import de.commons.lib.spark.environments.SparkR.SparkEnvironment
import de.lightningpayments.{SparkTestSupport, TestSpec}

class IterationsSpec extends TestSpec with SparkTestSupport {

  "Iterations#run" must {
    "return a set of iterations as count" in withSparkSession { _ => logger =>
      val program = Iterations.run.map(_.collect().toList.map(_.value))
      whenReady(program.provide(new SparkEnvironment(configuration, logger)))(_ mustBe Right(
        1 :: 2 :: 3 :: 4 :: 5 :: 6 :: 7 :: 8 :: 9 :: 10 :: Nil
      ))
    }
  }

}
