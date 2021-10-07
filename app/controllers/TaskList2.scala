package controllers

import javax.inject._
import play.api.mvc._
import play.api.i18n._
import models.TaskListInMemoryModel

@Singleton
class TaskList2 @Inject()(cc: ControllerComponents) extends AbstractController(cc){

    def load=Action{ implicit request=>
      request.session.get("username").map { username =>              
        Ok(views.html.tasklist2(TaskListInMemoryModel.getTasks(username)))
      }.getOrElse(Ok(views.html.version2Main()))
        
        
    }

    def login=Action{ implicit request=>
        Ok(views.html.login2())
    }
    def validate(username:String,password:String)=Action{ implicit request=>
        
        if (TaskListInMemoryModel.validateUser(username, password))
        Ok(views.html.tasklist2(TaskListInMemoryModel.getTasks(username))).withSession("username" -> username)
      else
        Ok(views.html.login2())
    }

    def create(username:String,password:String)=Action{ implicit request=>
        
        if (TaskListInMemoryModel.createUser(username, password))
        Ok(views.html.tasklist2(TaskListInMemoryModel.getTasks(username))).withSession("username" -> username)
      else
        Ok(views.html.login2())
    }

    def deleteTask(index:String)=Action{ implicit request=>
      request.session.get("username").map { username=>
        if (TaskListInMemoryModel.removeTask(username,index.toInt))
        Ok(views.html.tasklist2(TaskListInMemoryModel.getTasks(username))).withSession("username" -> username)
      else
        Ok(views.html.login2())
      }.getOrElse(Redirect(routes.TaskList2.login))
    }

    def addTask(task:String) = Action { implicit request =>
    request.session.get("username").map { username =>              
        TaskListInMemoryModel.addTask(username, task)
        Ok(views.html.tasklist2(TaskListInMemoryModel.getTasks(username)))    
    }.getOrElse(Redirect(routes.TaskList2.login))
  }

   def logout = Action {
    Redirect(routes.TaskList2.load).withNewSession
  }

}