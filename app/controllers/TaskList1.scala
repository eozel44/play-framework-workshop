package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class TaskList1 @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def taskList = Action{
    val tasks = List("task 1","task 2","task 3")
    Ok(views.html.TaskList1(tasks))
  }

}
