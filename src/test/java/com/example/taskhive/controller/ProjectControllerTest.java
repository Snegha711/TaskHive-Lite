package com.example.taskhive.controller;

import com.example.taskhive.entity.Project;
import com.example.taskhive.entity.User;
import com.example.taskhive.exception.InvalidInputException;
import com.example.taskhive.exception.ResourceNotFoundException;
import com.example.taskhive.service.ProjectService;
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

class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectController projectController;

    private Project project1;
    private Project project2;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        project1 = new Project();
        project1.setId(1L);
        project1.setName("Project One");

        project2 = new Project();
        project2.setId(2L);
        project2.setName("Project Two");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    void getAllProjects_ShouldReturnAllProjects() {
        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project1, project2));

        ResponseEntity<List<Project>> response = projectController.getAllProjects();

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void getProjectById_ShouldReturnProject_WhenFound() {
        when(projectService.getProjectById(1L)).thenReturn(Optional.of(project1));

        ResponseEntity<Project> response = projectController.getProjectById(1L);

        assertNotNull(response);
        assertEquals("Project One", response.getBody().getName());
        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    void getProjectById_ShouldThrowException_WhenNotFound() {
        when(projectService.getProjectById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectController.getProjectById(1L));
        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    void getProjectsByOwner_ShouldReturnProjects_WhenUserExists() {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(projectService.getProjectsByOwner(user)).thenReturn(Arrays.asList(project1));

        ResponseEntity<List<Project>> response = projectController.getProjectsByOwner(1L);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getUserById(1L);
        verify(projectService, times(1)).getProjectsByOwner(user);
    }

    @Test
    void getProjectsByOwner_ShouldThrowException_WhenUserNotFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectController.getProjectsByOwner(1L));
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void createProject_ShouldReturnCreatedProject_WhenValid() {
        when(projectService.createProject(project1)).thenReturn(project1);

        ResponseEntity<Project> response = projectController.createProject(project1);

        assertNotNull(response);
        assertEquals("Project One", response.getBody().getName());
        verify(projectService, times(1)).createProject(project1);
    }

    @Test
    void createProject_ShouldThrowException_WhenInvalid() {
        Project invalidProject = new Project();

        assertThrows(InvalidInputException.class, () -> projectController.createProject(invalidProject));
        verify(projectService, times(0)).createProject(any());
    }

    @Test
    void updateProject_ShouldReturnUpdatedProject_WhenValid() {
        when(projectService.updateProject(eq(1L), any(Project.class))).thenReturn(project1);

        ResponseEntity<Project> response = projectController.updateProject(1L, project1);

        assertNotNull(response);
        assertEquals("Project One", response.getBody().getName());
        verify(projectService, times(1)).updateProject(eq(1L), any(Project.class));
    }

    @Test
    void updateProject_ShouldThrowException_WhenInvalid() {
        Project invalidProject = new Project();

        assertThrows(InvalidInputException.class, () -> projectController.updateProject(1L, invalidProject));
        verify(projectService, times(0)).updateProject(anyLong(), any());
    }

    @Test
    void deleteProject_ShouldDeleteProject_WhenExists() {
        when(projectService.getProjectById(1L)).thenReturn(Optional.of(project1));

        ResponseEntity<Void> response = projectController.deleteProject(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(projectService, times(1)).deleteProject(1L);
    }

    @Test
    void deleteProject_ShouldThrowException_WhenNotFound() {
        when(projectService.getProjectById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectController.deleteProject(1L));
        verify(projectService, times(0)).deleteProject(anyLong());
    }
}
