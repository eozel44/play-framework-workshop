package controllers

import models.TaskListInMemoryModel

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.routing._

@Singleton
class TaskList @Inject()(val controllerComponents: ControllerComponents) extends BaseController {


  def login = Action {
    Ok(views.html.login())
  }

  def taskList = Action {
    val username="eren"
    val tasks = TaskListInMemoryModel.getTasks(username)
    Ok(views.html.TaskList(tasks))
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
        Redirect(routes.TaskList.taskList)
      else
        Redirect(routes.TaskList.login)

    }.getOrElse(Redirect(routes.TaskList.login))
  }

  def createUser =Action{ request=>
    request.body.asFormUrlEncoded.map{args=>
      if(TaskListInMemoryModel.createUser(args("username").head,args("password").head))
        Redirect(routes.TaskList.taskList)
      else
        Redirect(routes.TaskList.login)
    }.getOrElse(Redirect(routes.TaskList.login))
  }



}
