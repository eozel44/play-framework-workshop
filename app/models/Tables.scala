package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Tasks.schema ++ Users.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /**
   * Entity class storing rows of table Tasks
   *  @param taskId Database column task_id SqlType(INT), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(INT)
   *  @param text Database column text SqlType(VARCHAR), Length(2000,true)
   */
  case class TasksRow(taskId: Int, userId: Int, text: String)

  /** GetResult implicit for fetching TasksRow objects using plain SQL queries */
  implicit def GetResultTasksRow(implicit e0: GR[Int], e1: GR[String]): GR[TasksRow] = GR { prs =>
    import prs._
    TasksRow.tupled((<<[Int], <<[Int], <<[String]))
  }

  /** Table description of table tasks. Objects of this class serve as prototypes for rows in queries. */
  class Tasks(_tableTag: Tag) extends profile.api.Table[TasksRow](_tableTag, Some("playwithscala"), "tasks") {
    def * = (taskId, userId, text) <> (TasksRow.tupled, TasksRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(taskId), Rep.Some(userId), Rep.Some(text))).shaped.<>(
      { r => import r._; _1.map(_ => TasksRow.tupled((_1.get, _2.get, _3.get))) },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    /** Database column task_id SqlType(INT), AutoInc, PrimaryKey */
    val taskId: Rep[Int] = column[Int]("task_id", O.AutoInc, O.PrimaryKey)

    /** Database column user_id SqlType(INT) */
    val userId: Rep[Int] = column[Int]("user_id")

    /** Database column text SqlType(VARCHAR), Length(2000,true) */
    val text: Rep[String] = column[String]("text", O.Length(2000, varying = true))
  }

  /** Collection-like TableQuery object for table Tasks */
  lazy val Tasks = new TableQuery(tag => new Tasks(tag))

  /**
   * Entity class storing rows of table Users
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param username Database column username SqlType(VARCHAR), Length(20,true)
   *  @param password Database column password SqlType(VARCHAR), Length(200,true)
   */
  case class UsersRow(id: Int, username: String, password: String)

  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String]): GR[UsersRow] = GR { prs =>
    import prs._
    UsersRow.tupled((<<[Int], <<[String], <<[String]))
  }

  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, Some("playwithscala"), "users") {
    def * = (id, username, password) <> (UsersRow.tupled, UsersRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(username), Rep.Some(password))).shaped.<>(
      { r => import r._; _1.map(_ => UsersRow.tupled((_1.get, _2.get, _3.get))) },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

    /** Database column username SqlType(VARCHAR), Length(20,true) */
    val username: Rep[String] = column[String]("username", O.Length(20, varying = true))

    /** Database column password SqlType(VARCHAR), Length(200,true) */
    val password: Rep[String] = column[String]("password", O.Length(200, varying = true))
  }

  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
