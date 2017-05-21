package services.task

import org.mockito.Mockito._
import org.junit.Test
import org.junit.Assert._
import repositories.task.TaskRepositoryComponent
import domain.task.{Task, TaskStatus}

class TaskServiceTest extends TaskServiceComponentImpl
                      with TaskRepositoryMockComponent {

    val id = Option(1L)
    val subject = "Test subject 1"
    val content = "Test content 1"
    val status = TaskStatus.Pending
    
    @Test
    def createTask() {
        val task = Task(Option.empty, subject, content, status)
        
        taskService.createTask(task)
        
        verify(taskRepository).createTask(task)
    }
    
    @Test
    def updateTask() {
        val task = Task(id, subject, content, status)
                
        taskService.updateTask(task)
                
        verify(taskRepository).updateTask(task)
    }

    @Test
    def findAllTask() {
        taskService.findAllTask()

        verify(taskRepository).findAllTask()
    }
    
    @Test
    def findByIdDefined() {
        val this_id = 1L
        val task = Task(Option(this_id), subject, content, status)
        when(taskRepository.tryFindById(this_id)).thenReturn(Option(task))
                
        val retrievedTask = taskService.tryFindById(this_id)
        
        assertEquals(task, retrievedTask.get)
        verify(taskRepository).tryFindById(this_id)
    }
    
    @Test
    def findByIdEmpty() {
        val this_id = 1L
        when(taskRepository.tryFindById(this_id)).thenReturn(Option.empty)
        
        val retrievedTask = taskService.tryFindById(this_id)
        
        assertEquals(Option.empty, retrievedTask)
        verify(taskRepository).tryFindById(this_id)
    }

    @Test
    def updateTaskStatus() {
        val this_id = 1L
        val this_status = TaskStatus.Done

        when(taskService.updateStatus(this_id, this_status.id)).thenReturn(Option.empty)

        assertEquals(this_status, this_status)
    }
    
    @Test
    def delete() {
        val id = 1L

        taskService.delete(id)
        
        verify(taskRepository).delete(id)
    }

}

trait TaskRepositoryMockComponent extends TaskRepositoryComponent {
    
    override val taskRepository = mock(classOf[TaskRepository])
    
}
