package services.task

import domain.task.Task
import repositories.task.TaskRepositoryComponent

trait TaskServiceComponent {
    
    val taskService: TaskService
    
    trait TaskService {
        
        def createTask(task: Task): Task
        
        def updateTask(task: Task): Task

        def findAllTask(): Iterable[Task]
        
        def tryFindById(id: Long): Option[Task]

        def updateStatus(id: Long, status: Int): Option[Task]
        
        def delete(id: Long)
    
    }

}

trait TaskServiceComponentImpl extends TaskServiceComponent {
    self: TaskRepositoryComponent =>
    
    override val taskService = new TaskServiceImpl
    
    class TaskServiceImpl extends TaskService {
        
        override def createTask(task: Task): Task = {
            taskRepository.createTask(task)
        }
        
        override def updateTask(task: Task): Task = {
            taskRepository.updateTask(task)
        }

        override def findAllTask(): Iterable[Task] = {
            taskRepository.findAllTask()
        }
        
        override def tryFindById(id: Long): Option[Task] = {
            taskRepository.tryFindById(id)
        }

        override def updateStatus(id: Long, status: Int): Option[Task] = {
            taskRepository.updateStatus(id, status)
        }
        
        override def delete(id: Long) {
            taskRepository.delete(id)
        }
        
    }
}
