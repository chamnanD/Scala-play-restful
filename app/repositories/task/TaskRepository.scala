package repositories.task

import domain.task.{Task, TaskStatus}
import java.util.concurrent.atomic.AtomicLong

import domain.task.TaskStatus.TaskStatus

import scala.collection.mutable

trait TaskRepositoryComponent {
    val taskRepository: TaskRepository
    
    trait TaskRepository {

        def createTask(task: Task): Task

        def updateTask(task: Task): Task

        def updateStatus(id: Long, status: Int): Option[Task]

        def findAllTask(): Iterable[Task]

        def tryFindById(id: Long): Option[Task]

        def delete(id: Long)
        
    }
}

trait TaskRepositoryComponentImpl extends TaskRepositoryComponent {
    override val taskRepository = new TaskRepositoryImpl
    
    class TaskRepositoryImpl extends TaskRepository {
        
        val tasks = new mutable.LinkedHashMap[Long, Task]
        tasks.put(1, new Task(Some(1), "Subject1", "Content1", TaskStatus.Pending))
        tasks.put(2, new Task(Some(2), "Subject2", "Content2", TaskStatus.Done))
        tasks.put(3, new Task(Some(3), "Subject3", "Content3", TaskStatus.Pending))
        tasks.put(4, new Task(Some(4), "Subject4", "Content4", TaskStatus.Done))
        val idSequence = new AtomicLong(4)
        
        override def createTask(task: Task): Task = {
            val newId = idSequence.incrementAndGet()
            val createdTask = task.copy(id = Option(newId))
            tasks.put(newId, createdTask)
            createdTask
        }
        
        override def updateTask(task: Task): Task = {
            tasks.put(task.id.getOrElse(0), task)
            task
        }

        override def findAllTask(): Iterable[Task] = {
            tasks.values.toSeq.sortBy(_.id)
        }
        
        override def tryFindById(id: Long): Option[Task] = {
            tasks.get(id)
        }

        override def updateStatus(id: Long, status: Int): Option[Task] = {
            val taskStatus: TaskStatus = if (status == 1) TaskStatus.Done else TaskStatus.Pending
            val oldTask = tryFindById(id)
            if (oldTask.isDefined) {
                val newTask = Task(oldTask.get.id, oldTask.get.subject, oldTask.get.content, taskStatus)
                tasks.put(id, newTask)
                Option(newTask)
            } else {
                oldTask
            }
        }
        
        override def delete(id: Long) {
            tasks.remove(id)
        }
        
    }
    
}