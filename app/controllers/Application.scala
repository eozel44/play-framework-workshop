package controllers

import play.api.mvc._

import javax.inject._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class Application @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index("hello malatya"))
  }

  def product(prodType: String, prodNum: Int): Action[AnyContent] = Action {
    Ok(s"Product Type: $prodType, Product Number: $prodNum")
  }

  def randomNumber: Action[AnyContent] = Action {
    Ok(util.Random.nextInt(100).toString())

  }
}
