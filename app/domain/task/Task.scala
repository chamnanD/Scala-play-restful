package domain.task

import play.api.libs.json._
import domain.task.TaskStatus.TaskStatus

object TaskStatus extends Enumeration {
  type TaskStatus = Value
  val Pending = Value(0)
  val Done = Value(1)
}

case class Task(
                 id: Option[Long],
                 subject: String,
                 content: String,
                 status: TaskStatus
               )

object Task {
  implicit val taskWrites = new Writes[Task] {
    override def writes(task: Task): JsValue = {
      Json.obj(
        "id" -> task.id,
        "subject" -> task.subject,
        "content" -> task.content,
        "status" -> task.status.id
      )
    }
  }
}
