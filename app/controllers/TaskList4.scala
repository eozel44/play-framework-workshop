package controllers

import javax.inject._
import play.api.mvc._
import play.api.i18n._
import play.api.libs.json._
import models._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.ExecutionContext
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.Future

@Singleton
class TaskList4 @Inject() (
    protected val dbConfigProvider: DatabaseConfigProvider,
    cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with HasDatabaseConfigProvider[JdbcProfile] {      

  private val model = new TaskListDatabaseModel(db)

  def load = Action { implicit request =>
    Ok(views.html.version4Main())
  }

  implicit val userDataReads = Json.reads[UserData]

  def withJsonBody[A](
      f: A => Future[Result]
  )(implicit request: Request[AnyContent], reads: Reads[A]) = {
    request.body.asJson
      .map { body =>
        Json.fromJson[A](body) match {
          case JsSuccess(a, path) => f(a)
          case e @ JsError(_)     => Future.successful(Redirect(routes.TaskList4.load))
        }
      }
      .getOrElse(Future.successful(Redirect(routes.TaskList4.load)))
  }

  def withSessionUsername(
      f: String => Future[Result]
  )(implicit request: Request[AnyContent]) = {
    request.session
      .get("username")
      .map(f)
      .getOrElse(Future.successful(Ok(Json.toJson(Seq.empty[String]))))
  }

  def validate = Action.async { implicit request =>
    withJsonBody[UserData] { ud =>
      model.validateUser(ud.username, ud.password).map { userExits =>

        if(userExits){
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
}

  def tasklist = TODO 
    // Action.async { implicit request =>
  //   withSessionUsername { username =>
  //     Ok(Json.toJson(model.getTasks(username)))
  //   }
  // }

  def createUser = Action.async { implicit request =>
    withJsonBody[UserData] { ud =>
      model.createUser(ud.username, ud.password).map { userCreated=>
        if(userCreated){
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
  }

  def addTask = TODO 
  //   Action.async { implicit request =>
  //   withSessionUsername { username =>
  //     withJsonBody[String] { task =>
  //       model.addTask(username, task);
  //       Ok(Json.toJson(true))
  //     }
  //   }
  // }

  def delete = TODO
    
  //   Action.async { implicit request =>
  //   withSessionUsername { username =>
  //     withJsonBody[Int] { index =>
  //       model.removeTask(username, index)
  //       Ok(Json.toJson(true))
  //     }
  //   }
  // }

  /** * before refactoring
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
          case e @ JsError(_) => Redirect(routes.TaskList4.load())
        }
      }.getOrElse(Ok(Json.toJson(false)))
    }.getOrElse(Ok(Json.toJson(false)))
  }
   */

  def logout = Action { implicit request =>
    Ok(Json.toJson(true)).withSession(request.session - "username")
  }

}
