package com.example.taskhive.controller;

import com.example.taskhive.entity.Task;
import com.example.taskhive.entity.User;
import com.example.taskhive.exception.InvalidInputException;
import com.example.taskhive.exception.ResourceNotFoundException;
import com.example.taskhive.service.TaskService;
import com.example.taskhive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTask() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        // If the task is not found, throw ResourceNotFoundException
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        // If user is not found, throw ResourceNotFoundException
        Optional<User> user = userService.getUserById(userId);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return ResponseEntity.ok(taskService.getTasksByUser(user.get()));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        // Validate the task name or other required fields
        if (task.getAssignedTo() == null || task.getDescription().isEmpty() ||task.getTitle().isEmpty()) {
            throw new InvalidInputException("Task name is required");
        }
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        // Validate the task name or other required fields
        if (updatedTask.getAssignedTo() == null ||
                updatedTask.getDescription().isEmpty() ||
                updatedTask.getTitle().isEmpty()){
            throw new InvalidInputException("Task details required for update");
        }
        return ResponseEntity.ok(taskService.updateTask(id, updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        // If the task does not exist, throw ResourceNotFoundException
        if (!taskService.getTaskById(id).isPresent()) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
