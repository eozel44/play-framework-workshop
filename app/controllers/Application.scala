package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index("hello malatya"))
  }

  def product(prodType:String,prodNum:Int)=Action{
    Ok(s"Product Type: $prodType, Product Number: $prodNum")
  }

  def randomNumber=Action{
    Ok(util.Random.nextInt(100).toString())

  }
}
