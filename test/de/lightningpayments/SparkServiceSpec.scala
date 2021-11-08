package de.lightningpayments

import de.lightningpayments.app.SparkService

class SparkServiceSpec extends TestSpec with SparkTestSupport {

  "SparkService#run" must {
    "return a unit" in {
      val service = new SparkService(configuration)
      whenReady(service.run)(_ mustBe Right(()))
    }
  }

}
