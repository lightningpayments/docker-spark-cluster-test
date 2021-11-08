package de.lightningpayments

class SparkServiceSpec extends TestSpec with SparkTestSupport {

  "SparkService#run" must {
    "return a unit" in {
      val service = new SparkService(configuration)
      whenReady(service.run)(_.isRight mustBe true)
    }
  }

}
