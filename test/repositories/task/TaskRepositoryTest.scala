package repositories.task

import org.junit.Test
import org.junit.Assert._
import domain.task.{Task, TaskStatus}

class TaskRepositoryTest extends TaskRepositoryComponentImpl {
    
    @Test
    def crud() {
        // Creation.
        val subject = "Test subject 1"
        val content = "Test content 1"
        val status = TaskStatus.Pending
        val task = Task(Option.empty, subject, content, status)
        val createdTask = taskRepository.createTask(task)
        
        assertTrue(createdTask.id.isDefined)
        
        // Retrieval.
        var retrievedTask = taskRepository.tryFindById(createdTask.id.get)
        assertTrue(retrievedTask.isDefined)
        assertEquals(createdTask.id, retrievedTask.get.id)
        assertEquals(subject, retrievedTask.get.subject)
        assertEquals(content, retrievedTask.get.content)
        assertEquals(status, retrievedTask.get.status)
        
        // Update, retrieval and check.
        val updatedStatus = TaskStatus.Done
        val updatedTask = Task(createdTask.id, subject, content, updatedStatus)
        taskRepository.updateTask(updatedTask)
        
        retrievedTask = taskRepository.tryFindById(createdTask.id.get)
        assertTrue(retrievedTask.isDefined)
        assertEquals(updatedTask.id, retrievedTask.get.id)
        assertEquals(updatedStatus, retrievedTask.get.status)
        
        // Delete.
        taskRepository.delete(createdTask.id.get)
        assertTrue(taskRepository.tryFindById(createdTask.id.get).isEmpty)
    }
    
}