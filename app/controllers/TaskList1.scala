package controllers

import models.TaskListInMemoryModel

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class TaskList1 @Inject()(val controllerComponents: ControllerComponents) extends BaseController {


  def login = Action {
    Ok(views.html.login1())
  }

  def taskList = Action {
    val username="eren"
    val tasks = TaskListInMemoryModel.getTasks(username)
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

      if(TaskListInMemoryModel.validateUser(username,password))
        Redirect(routes.TaskList1.taskList)
      else
        Redirect(routes.TaskList1.login)

    }.getOrElse(Redirect(routes.TaskList1.login))
  }

  def createUser =Action{ request=>
    request.body.asFormUrlEncoded.map{args=>
      if(TaskListInMemoryModel.createUser(args("username").head,args("password").head))
        Redirect(routes.TaskList1.taskList)
      else
        Redirect(routes.TaskList1.login)
    }.getOrElse(Redirect(routes.TaskList1.login))*



  }



}
