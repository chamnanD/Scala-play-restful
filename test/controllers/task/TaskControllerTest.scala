package controllers.task

import scala.concurrent._
import org.junit._
import org.junit.Assert._
import org.mockito.Mockito._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import services.task.TaskServiceComponent
import domain.task.{Task, TaskStatus}

class TaskControllerTest {

    private val taskController = new TaskController with TaskServiceComponentMock {}

    val id: Long = 1
    val subject = "Test subject controller 1"
    val content = "Test content controller 1"
    val status = TaskStatus.Pending

    val rightFormat = Json.obj(
        "subject" -> subject,
        "content" -> content,
        "status"  -> status.id
    )

    val wrongFormatWithInvalidStatus = Json.obj(
        "subject" -> subject,
        "content" -> content,
        "status"  -> 99
    )

    @Test
    def createTaskWithSuccess() {
        val request = FakeRequest(POST, "/tasks").withBody(rightFormat)

        val result: Future[Result] = taskController.createTask(request)

        assertEquals(TaskAction.Success.toString(), contentAsString(result))

        verify(taskController.taskService).createTask(Task(Option.empty, subject, content, status))
    }

    @Test
    def createTaskWithInvalidStatus() {
        val request = FakeRequest(POST, "/tasks").withBody(wrongFormatWithInvalidStatus)

        val result: Future[Result] = taskController.createTask(request)

        assertEquals(TaskAction.InvalidStatus.toString(), contentAsString(result))

    }

}

trait TaskServiceComponentMock extends TaskServiceComponent {

    override val taskService = mock(classOf[TaskService])

}
