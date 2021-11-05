package de.lightningpayments

import de.lightningpayments.app.{SimpleApp, SparkService}
import org.mockito.IdiomaticMockito.StubbingOps
import org.mockito.MockitoSugar.mock
import play.api.test.FakeRequest
import play.api.test.Helpers._
import zio.IO

class SimpleAppSpec extends TestSpec with SparkTestSupport {

  "SimpleApp#spark" must {
    "run a the app on a spark cluster" in {
      val service = mock[SparkService]
      val controller = new SimpleApp(stubControllerComponents(), service)

      service.run returns IO.unit

      val result = controller.spark.apply(FakeRequest())
      status(result) mustBe NO_CONTENT
    }
  }

}
