package models

import slick.jdbc.MySQLProfile.api._
import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future
import com.typesafe.sslconfig.ssl.FakeChainedKeyStore

class TaskListDatabaseModel(db: Database)(implicit ec: ExecutionContext) {

  def getTasks(username: String): Future[Seq[String]] = {
    db.run(
      (for {
        user <- Users if user.username === username
        task <- Tasks if task.userId === user.id
      } yield (task)).result
    ).map(tasks => tasks.map(task=> task.text))
  }

  def addTask(userId: Int, task: String): Future[Int] = {
    db.run(Tasks += TasksRow(-1, userId, task))
  }

  def removeTask(taskId: Int): Future[Boolean] = {
    db.run( Tasks.filter(_.taskId === taskId).delete ).map(count => count > 0)
  }

  def validateUser(username: String, password: String): Future[Option[Int]] = {
    val matches = db.run(Users.filter(userRow => userRow.username === username).result)
    matches.map(userRows => userRows.headOption.flatMap {
      userRow => if (password == userRow.password) Some(userRow.id) else None
    })
  }

  def createUser(username: String, password: String): Future[Boolean] = {
    db.run(Users += UsersRow(-1, username, password))
      .map(addCount => addCount > 0)

  }

}
