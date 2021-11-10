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

    def withSessionUserid(f: Int => Future[Result])(implicit request: Request[AnyContent]): Future[Result] = {
    request.session.get("userid").map(userid => f(userid.toInt)).getOrElse(Future.successful(Ok(Json.toJson(Seq.empty[String]))))
  }

  def validate = Action.async { implicit request =>
    withJsonBody[UserData] { ud =>
      model.validateUser(ud.username, ud.password).map { ouserId =>
        ouserId match {
          case Some(userid) =>
            Ok(Json.toJson(true))
              .withSession("username" -> ud.username, "userid" -> userid.toString, "csrfToken" -> play.filters.csrf.CSRF.getToken.map(_.value).getOrElse(""))
          case None =>
            Ok(Json.toJson(false))
        }
      }
    }
  }

  def tasklist = Action.async { implicit request =>
    withSessionUsername { username =>
      model.getTasks(username).map(tasks => Ok(Json.toJson(tasks)))
    }
  }

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

    def addTask = Action.async { implicit request =>
    withSessionUserid { userid =>
      withJsonBody[String] { task =>
        model.addTask(userid, task).map(count => Ok(Json.toJson(count > 0)))
      }
    }
  }
  //TODO: need refactor javascipt to send itemId
  def delete = Action.async { implicit request =>
    withSessionUsername { username =>
      withJsonBody[Int] { itemId =>
        model.removeTask(itemId).map(removed => Ok(Json.toJson(removed)))
      }
    }
  }

  def logout = Action { implicit request =>
    Ok(Json.toJson(true)).withSession(request.session - "username")
  }

}
