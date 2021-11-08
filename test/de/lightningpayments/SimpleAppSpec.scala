package de.lightningpayments

import de.lightningpayments.app.{SimpleApp, SparkService}
import org.mockito.IdiomaticMockito.StubbingOps
import org.mockito.MockitoSugar.mock
import play.api.test.FakeRequest
import play.api.test.Helpers._
import zio.Task

class SimpleAppSpec extends TestSpec with SparkTestSupport {

  "SimpleApp#spark" must {
    "run a the app on a spark cluster" in {
      val service = mock[SparkService]
      val controller = new SimpleApp(stubControllerComponents(), service)

      service.run returns Task(3.14)

      val result = controller.spark.apply(FakeRequest())
      status(result) mustBe OK
    }
    "returns an error" in {
      val service    = mock[SparkService]
      val error      = new Throwable("error")
      val controller = new SimpleApp(stubControllerComponents(), service)

      service.run returns Task.fail(error)

      val result = controller.spark.apply(FakeRequest())
      status(result) mustBe INTERNAL_SERVER_ERROR
    }
  }

}
