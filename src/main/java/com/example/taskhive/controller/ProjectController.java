package com.example.taskhive.controller;

import com.example.taskhive.entity.Project;
import com.example.taskhive.entity.User;
import com.example.taskhive.exception.InvalidInputException;
import com.example.taskhive.exception.ResourceNotFoundException;
import com.example.taskhive.service.ProjectService;
import com.example.taskhive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        // Check if project exists, if not throw ResourceNotFoundException
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    @GetMapping("/owner/{userId}")
    public ResponseEntity<List<Project>> getProjectsByOwner(@PathVariable Long userId) {
        // Check if user exists, if not throw ResourceNotFoundException
        Optional<User> owner = userService.getUserById(userId);
        if (!owner.isPresent()) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return ResponseEntity.ok(projectService.getProjectsByOwner(owner.get()));
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        // Check if project name is missing or invalid (basic validation)
        if (project.getName() == null || project.getName().isEmpty()) {
            throw new InvalidInputException("Project name is required");
        }
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        // Validate updated project fields (e.g., project name)
        if (updatedProject.getName() == null || updatedProject.getName().isEmpty()) {
            throw new InvalidInputException("Project name is required for update");
        }
        return ResponseEntity.ok(projectService.updateProject(id, updatedProject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        // Check if project exists before deleting
        if (!projectService.getProjectById(id).isPresent()) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
