package com.example.taskhive.controller;

import com.example.taskhive.entity.Task;
import com.example.taskhive.entity.User;
import com.example.taskhive.exception.InvalidInputException;
import com.example.taskhive.exception.ResourceNotFoundException;
import com.example.taskhive.service.TaskService;
import com.example.taskhive.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        when(taskService.getAllTasks()).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.getAllTasks();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tasks, response.getBody());
    }

    @Test
    void testGetTaskById_Success() {
        Task task = new Task();
        task.setId(1L);
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        ResponseEntity<Task> response = taskController.getTaskById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task, response.getBody());
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskController.getTaskById(1L);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
    }

    @Test
    void testGetTasksByUser_Success() {
        User user = new User();
        user.setId(1L);
        List<Task> tasks = Arrays.asList(new Task(), new Task());

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(taskService.getTasksByUser(user)).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.getTasksByUser(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tasks, response.getBody());
    }

    @Test
    void testGetTasksByUser_UserNotFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskController.getTasksByUser(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void testCreateTask_Success() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setAssignedTo(new User());

        when(taskService.createTask(task)).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(task);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(task, response.getBody());
    }

    @Test
    void testCreateTask_InvalidInput() {
        Task task = new Task();
        task.setTitle("");
        task.setDescription("");

        Exception exception = assertThrows(InvalidInputException.class, () -> {
            taskController.createTask(task);
        });

        assertEquals("Task name is required", exception.getMessage());
    }

    @Test
    void testUpdateTask_Success() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setAssignedTo(new User());

        when(taskService.updateTask(1L, updatedTask)).thenReturn(updatedTask);

        ResponseEntity<Task> response = taskController.updateTask(1L, updatedTask);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedTask, response.getBody());
    }

    @Test
    void testUpdateTask_InvalidInput() {
        Task updatedTask = new Task();
        updatedTask.setTitle("");
        updatedTask.setDescription("");

        Exception exception = assertThrows(InvalidInputException.class, () -> {
            taskController.updateTask(1L, updatedTask);
        });

        assertEquals("Task details required for update", exception.getMessage());
    }

    @Test
    void testDeleteTask_Success() {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(new Task()));

        ResponseEntity<Void> response = taskController.deleteTask(1L);

        verify(taskService, times(1)).deleteTask(1L);
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskController.deleteTask(1L);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
    }

}
