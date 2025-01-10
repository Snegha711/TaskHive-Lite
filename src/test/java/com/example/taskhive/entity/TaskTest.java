package com.example.taskhive.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskCreation() {
        User assignedTo = new User();
        assignedTo.setId(2L);
        assignedTo.setUsername("user1");
        assignedTo.setEmail("user@example.com");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
        task.setStatus(Task.Status.PENDING);
        task.setDueDate(LocalDateTime.now().plusDays(1));
        task.setAssignedTo(assignedTo);
        task.setProject(project);

        assertNotNull(task);
        assertEquals("Test Task", task.getTitle());
        assertEquals("This is a test task", task.getDescription());
        assertEquals(Task.Status.PENDING, task.getStatus());
        assertNotNull(task.getDueDate());
        assertEquals(assignedTo, task.getAssignedTo());
        assertEquals(project, task.getProject());
    }

    @Test
    void testTaskEnum() {
        Task.Status status = Task.Status.PENDING;
        assertEquals(Task.Status.PENDING, status);
    }
}
