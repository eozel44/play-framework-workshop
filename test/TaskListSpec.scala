import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.OneBrowserPerSuite
import org.scalatestplus.play.HtmlUnitFactory
import org.scalatestplus.play.guice.GuiceOneServerPerSuite

class TaskListSpec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory{
    "TaskList" must{

        "login and see task list" in {
      go to s"http://localhost:$port/login"
      pageTitle mustBe "Login"
      find(cssSelector("h2")).isEmpty mustBe false
      find(cssSelector("h2")).foreach(e => e.text mustBe "Login")
      click on "username-login"
      textField("username-login").value = "eren"
      click on "password-login"
      pwdField(id("password-login")).value = "123"
      submit()
      eventually {
        pageTitle mustBe "Task List"
        find(cssSelector("h2")).isEmpty mustBe false
        find(cssSelector("h2")).foreach(e => e.text mustBe "Task List")
        findAll(cssSelector("li")).toList.map(_.text) mustBe List("eat", "sleep", "code")
      }
    }


    }


}