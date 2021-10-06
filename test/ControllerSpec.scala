import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.test.Helpers
import controllers.Application

class ControllerSpec extends PlaySpec{

    "The Application controller" must {
    val controller = new Application(Helpers.stubControllerComponents())
    "give back expected index page" in {
      val result = controller.index.apply(FakeRequest())
      val bodyText = contentAsString(result)
      bodyText must include ("Play")
      bodyText must include ("Welcome")
    }

    "give back a product" in {
      val result = controller.product("test", 42).apply(FakeRequest())
      val bodyText = contentAsString(result)
      bodyText mustBe ("Product Type: test, Product Number: 42")
    }


}

    
}