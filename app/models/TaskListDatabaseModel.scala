package models

import slick.jdbc.MySQLProfile.api._
import scala.concurrent.ExecutionContext
import models.Tables._
import scala.concurrent.Future

class TaskListDatabaseModel(db: Database)(implicit ec: ExecutionContext) {

  def getTasks(username: String): Future[Seq[String]] = ???

  def addTask(username: String, task: String): Future[Int] = ???

  def removeTask(username: String, index: Int): Future[Boolean] = ???

  def validateUser(username: String, password: String): Future[Boolean] = {
    val matches: Future[Seq[UsersRow]] = db.run(
      Users
        .filter(userRow =>
          userRow.username === username && userRow.password === password
        )
        .result
    )
    matches.map(userRows => userRows.nonEmpty)    
  }

  def createUser(username: String, password: String): Future[Boolean] = {
    db.run(Users += UsersRow(-1,username,password)).map(addCount => addCount>0) 

  }

}
