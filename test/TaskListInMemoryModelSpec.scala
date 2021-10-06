import org.scalatestplus.play.PlaySpec
import models.TaskListInMemoryModel
import views.html.TaskList

class TaskListInMemoryModelSpec extends PlaySpec{
    "TaskListInMemoryModel" must{
        "do valid login for default user" in{
            TaskListInMemoryModel.validateUser("eren","123") mustBe(true)
        }

        "reject login with wrong password" in{
            TaskListInMemoryModel.validateUser("eren","123456") mustBe(false)
        }

        "reject login with wrong username" in{
            TaskListInMemoryModel.validateUser("erenn","123") mustBe(false)
        }

        "reject login with wrong username and password" in{
            TaskListInMemoryModel.validateUser("erenn","12345") mustBe(false)
        }

        "get correct default tasks" in {
            TaskListInMemoryModel.getTasks("eren") mustBe(List("eat", "sleep", "code"))
        }

        "create new user with no tasks" in {

            TaskListInMemoryModel.createUser("asya","asya") mustBe(true)
            TaskListInMemoryModel.getTasks("asya") mustBe(Nil)
        }

        "add new task for default user" in {

            TaskListInMemoryModel.addTask("eren","testing")
            TaskListInMemoryModel.getTasks("eren") must contain ("testing") 
        }

        "add new task for new user" in{
            TaskListInMemoryModel.addTask("asya","testing")
            TaskListInMemoryModel.getTasks("asya") must contain ("testing") 
        }
        
        "remove task from default user" in{
            TaskListInMemoryModel.removeTask("eren",TaskListInMemoryModel.getTasks("eren").indexOf("eat"))
            TaskListInMemoryModel.getTasks("eren") must not contain ("eat") 
        }
        
    }

}