package com.example.taskhive.service;

import com.example.taskhive.entity.Task;
import com.example.taskhive.entity.User;
import com.example.taskhive.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus(Task.Status.PENDING);
        task1.setDueDate(LocalDateTime.now());

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Updated Task");
        task2.setDescription("Updated Description");
        task2.setStatus(Task.Status.IN_PROGRESS);
        task2.setDueDate(LocalDateTime.now());
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_shouldReturnTask_whenExists() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setDescription("Description 1");
        task.setStatus(Task.Status.PENDING);
        task.setDueDate(LocalDateTime.now());
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("Task 1", result.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_shouldReturnEmpty_whenNotExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.getTaskById(1L);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void createTask_shouldSaveAndReturnTask() {
        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Task 1");
        savedTask.setDescription("Description 1");
        savedTask.setStatus(Task.Status.PENDING);
        savedTask.setDueDate(LocalDateTime.now());

        Task task = new Task();
        task.setTitle("Updated Task");
        task.setDescription("Updated Description");
        task.setStatus(Task.Status.IN_PROGRESS);
        task.setDueDate(LocalDateTime.now());
        when(taskRepository.save(task)).thenReturn(savedTask);

        Task result = taskService.createTask(task);

        assertNotNull(result.getId());
        assertEquals("Task 1", result.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_shouldUpdateAndReturnTask_whenExists() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Task 1");
        existingTask.setDescription("Description 1");
        existingTask.setStatus(Task.Status.PENDING);
        existingTask.setDueDate(LocalDateTime.now());

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(Task.Status.IN_PROGRESS);
        updatedTask.setDueDate(LocalDateTime.now());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("Updated Task", result.getTitle());
        assertEquals(Task.Status.IN_PROGRESS, result.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_shouldThrowException_whenTaskNotFound() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(Task.Status.IN_PROGRESS);
        updatedTask.setDueDate(LocalDateTime.now());
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(1L, updatedTask));
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTask_shouldDeleteTask_whenExists() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}
