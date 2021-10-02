package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class TaskList1 @Inject()(val controllerComponents: ControllerComponents) extends BaseController {


  def login = Action {
    Ok(views.html.login1())
  }

  def taskList = Action {
    val tasks = List("task 1", "task 2", "task 3")
    Ok(views.html.TaskList1(tasks))
  }

  def validateLoginGet(username: String, password: String) = Action {

    Ok(s"username: $username logged in with $password")
  }

  //parameters encoding the body
  def validateLoginPost()= Action { request=>

    request.body.asFormUrlEncoded.map{ args =>
      val username = args("username").head
      val password = args("password").head
      Redirect(routes.TaskList1.taskList)
    }.getOrElse(Redirect(routes.TaskList1.login))
  }



}
