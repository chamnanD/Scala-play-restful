package controllers.task

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import services.task.TaskServiceComponent
import domain.task.{Task, TaskStatus}
import domain.task.TaskStatus.TaskStatus

case class TaskResource(
                         id: Option[Long],
                         subject: String,
                         content: String,
                         status: Option[Int]
                       )

object TaskAction extends Enumeration {
    type TaskAction = JsValue
    val Success = JsString("Success")
    val InvalidStatus = JsString("Status must be 0(Pending) or 1(Done)")
    val InvalidForm = JsString("Something went wrong please input correct format")
    val NotHaveTask = JsString("Not have Task ID")
    val UpdateSuccess = JsString("Update successfully")
    val DeleteSuccess = JsString("Delete successfully")
}

trait TaskController extends Controller {
    self: TaskServiceComponent =>

    def createTask = Action(parse.json) {request =>
        val subject: String = (request.body \ "subject").getOrElse(JsString("")).toString()
        val content: String = (request.body \ "content").getOrElse(JsString("")).toString()
        val statusInt: Int = (request.body \ "status").getOrElse(JsNumber(-1)).as[Int]
        if (isInvalid(subject, content, statusInt)) {
            BadRequest(TaskAction.InvalidForm)
        } else {
            val status: TaskStatus = if (statusInt == 1) TaskStatus.Done else TaskStatus.Pending
            val task = Task(Option.empty, subject, content, status)
            taskService.createTask(task)
            Ok(TaskAction.Success)
        }
    }

    def updateTask(id: Long) = Action(parse.json) {request =>
        val subject: String = (request.body \ "subject").getOrElse(JsString("")).toString()
        val content: String = (request.body \ "content").getOrElse(JsString("")).toString()
        val statusInt: Int = (request.body \ "status").getOrElse(JsNumber(-1)).as[Int]
        if (taskService.tryFindById(id).isEmpty) {
            BadRequest(TaskAction.NotHaveTask)
        } else if (isInvalid(subject, content, statusInt)) {
            BadRequest(TaskAction.InvalidForm)
        } else {
            val status: TaskStatus = if (statusInt == 1) TaskStatus.Done else TaskStatus.Pending
            val task = Task(Option(id), subject, content, status)
            taskService.updateTask(task)
            Ok(TaskAction.Success)
        }
    }

    def updateStatus(id: Long, status: Int) = Action {
        if (taskService.tryFindById(id).isEmpty) {
            BadRequest(TaskAction.NotHaveTask)
        } else if(status != 0 && status != 1) {
            BadRequest(TaskAction.InvalidStatus)
        } else {
            taskService.updateStatus(id, status)
            Ok(TaskAction.UpdateSuccess)
        }
    }

    def findAllTask() = Action {
        val task = taskService.findAllTask()
        Ok(Json.toJson(task))
    }

    def findTaskById(id: Long) = Action {
        val task = taskService.tryFindById(id)
        if (task.isDefined) {
            Ok(Json.toJson(task))
        } else {
            BadRequest(TaskAction.NotHaveTask)
        }
    }

    def deleteTask(id: Long) = Action {
        if (taskService.tryFindById(id).isDefined) {
            taskService.delete(id)
            Ok(TaskAction.DeleteSuccess)
        } else {
            BadRequest(TaskAction.NotHaveTask)
        }
    }

    def isInvalid(subject: String, content: String, status: Int): Boolean = {
        subject.isEmpty || content.isEmpty || status == -1
    }

}