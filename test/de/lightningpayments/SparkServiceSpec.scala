package de.lightningpayments

import de.lightningpayments.app.SparkService
import org.mockito.IdiomaticMockito.StubbingOps
import org.mockito.MockitoSugar.mock
import play.api.Configuration

class SparkServiceSpec extends TestSpec with SparkTestSupport {

  "SparkService#run" must {
    "return a unit" in {
      val service = new SparkService(configuration)
      whenReady(service.run)(_ mustBe Right(()))
    }
  }

}
