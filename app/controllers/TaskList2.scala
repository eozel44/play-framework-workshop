package controllers

import javax.inject._
import play.api.mvc._
import play.api.i18n._
import models.TaskListInMemoryModel

@Singleton
class TaskList2 @Inject()(cc: ControllerComponents) extends AbstractController(cc){

    def load=Action{ implicit request=>
        Ok(views.html.version2Main())
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

}