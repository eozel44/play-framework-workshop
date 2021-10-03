package controllers

import models.TaskListInMemoryModel

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.routing._

@Singleton
class TaskList @Inject()(val controllerComponents: ControllerComponents) extends BaseController {


  def login = Action {implicit request=>
    Ok(views.html.login())
  }

  def taskList = Action { implicit request=>
    request.session.get("username").map {username=>
      val tasks = TaskListInMemoryModel.getTasks(username)
      Ok(views.html.TaskList(tasks))
    }.getOrElse(Redirect(routes.TaskList.login))
  }

  def validateLoginGet(username: String, password: String) = Action {

    Ok(s"username: $username logged in with $password")
  }

  //parameters encoding the body
  def validateLoginPost()= Action { implicit request=>

    request.body.asFormUrlEncoded.map{ args =>
      val username = args("username").head
      val password = args("password").head

      if(TaskListInMemoryModel.validateUser(username,password))
        Redirect(routes.TaskList.taskList).withSession("username"->username)
      else
        Redirect(routes.TaskList.login)

    }.getOrElse(Redirect(routes.TaskList.login)).flashing("error" ->"Invalid username/password combination")
  }

  def createUser =Action{ implicit request=>
    request.body.asFormUrlEncoded.map{args=>
      val username =args("username").head
      val password = args("password").head
      if(TaskListInMemoryModel.createUser(username,password))
        Redirect(routes.TaskList.taskList).withSession("username"->username)
      else
        Redirect(routes.TaskList.login)
    }.getOrElse(Redirect(routes.TaskList.login)).flashing("error" ->"User creation failed.")
  }

  def logout=Action{
    Redirect(routes.TaskList.login).withNewSession
  }



}
