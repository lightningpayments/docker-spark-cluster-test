package de.lightningpayments.app.calculate

import de.lightningpayments.app.SimpleApp
import de.lightningpayments.{SparkTestSupport, TestSpec}
import zio.ExitCode

class SimpleAppSpec extends TestSpec with SparkTestSupport {

  "SimpleApp#run" must {
    "return an URIO" in {
      val runtime = zio.Runtime.default
      runtime.unsafeRun(SimpleApp.run(Nil)) mustBe a[ExitCode]
    }
  }

}
