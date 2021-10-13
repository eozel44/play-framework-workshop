package controllers

import javax.inject._
import play.api.mvc._
import play.api.i18n._
import models.TaskListInMemoryModel
import play.api.libs.json._
import models._

@Singleton
class TaskList3 @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {

  def load = Action { implicit request =>
    Ok(views.html.version3Main())
  }

  implicit val userDataReads = Json.reads[UserData]

  def withJsonBody[A](f: A => Result)(implicit request: Request[AnyContent], reads: Reads[A]) = {
    request.body.asJson
      .map { body =>
        Json.fromJson[A](body) match {
          case JsSuccess(a, path) => f(a)
          case e @ JsError(_)     => Redirect(routes.TaskList3.load)
        }
      }
      .getOrElse(Redirect(routes.TaskList3.load))
  }

  def withSessionUsername(f: String => Result)(implicit request: Request[AnyContent]) = {
    request.session
      .get("username")
      .map(f)
      .getOrElse(Ok(Json.toJson(Seq.empty[String])))
  }

  def validate = Action { implicit request =>
    withJsonBody[UserData] { ud =>
      if (TaskListInMemoryModel.validateUser(ud.username, ud.password)) {
        Ok(Json.toJson(true))
          .withSession(
            "username" -> ud.username,
            "csrfToken" -> play.filters.csrf.CSRF.getToken
              .map(_.value)
              .getOrElse("")
          )
      } else {
        Ok(Json.toJson(false))
      }
    }
  }

  def tasklist = Action { implicit request =>
    withSessionUsername { username =>
      Ok(Json.toJson(TaskListInMemoryModel.getTasks(username)))
    }
  }

  def createUser = Action { implicit request =>
    withJsonBody[UserData] { ud =>
      if (TaskListInMemoryModel.createUser(ud.username, ud.password)) {
        Ok(Json.toJson(true))
          .withSession(
            "username" -> ud.username,
            "csrfToken" -> play.filters.csrf.CSRF.getToken
              .map(_.value)
              .getOrElse("")
          )
      } else {
        Ok(Json.toJson(false))
      }
    }
  }

  def addTask = Action { implicit request =>
    withSessionUsername { username =>
      withJsonBody[String] { task =>
        TaskListInMemoryModel.addTask(username, task);
        Ok(Json.toJson(true))
      }
    }
  }

  def delete = Action { implicit request =>
    withSessionUsername { username =>
      withJsonBody[Int] { index =>
        TaskListInMemoryModel.removeTask(username, index)
        Ok(Json.toJson(true))
      }
    }
  }

  /***
   * before refactoring
   */
/*
   def delete = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      request.body.asJson.map { body =>
        Json.fromJson[Int](body) match {
          case JsSuccess(index, path) =>
            TaskListInMemoryModel.removeTask(username, index)
            Ok(Json.toJson(true))
          case e @ JsError(_) => Redirect(routes.TaskList3.load())
        }
      }.getOrElse(Ok(Json.toJson(false)))
    }.getOrElse(Ok(Json.toJson(false)))
  } 
*/

  def logout = Action { implicit request =>
    Ok(Json.toJson(true)).withSession(request.session - "username")
  }

}
