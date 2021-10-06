import org.scalatestplus.play.PlaySpec
import models.TaskListInMemoryModel

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

        "get correct default tasks" in {
            TaskListInMemoryModel.getTasks("eren") mustBe(List("eat", "sleep", "code"))
        }
    }

}