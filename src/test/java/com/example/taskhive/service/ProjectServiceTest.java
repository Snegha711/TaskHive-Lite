package com.example.taskhive.service;

import com.example.taskhive.entity.Project;
import com.example.taskhive.entity.User;
import com.example.taskhive.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getAllProjects_ShouldReturnListOfProjects() {
        // Arrange
        List<Project> mockProjects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findAll()).thenReturn(mockProjects);

        // Act
        List<Project> result = projectService.getAllProjects();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getProjectById_ShouldReturnProject_WhenProjectExists() {
        // Arrange
        Long projectId = 1L;
        Project mockProject = new Project();
        mockProject.setId(projectId);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockProject));

        // Act
        Optional<Project> result = projectService.getProjectById(projectId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(projectId, result.get().getId());
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    void getProjectById_ShouldReturnEmpty_WhenProjectDoesNotExist() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act
        Optional<Project> result = projectService.getProjectById(projectId);

        // Assert
        assertFalse(result.isPresent());
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    void getProjectsByOwner_ShouldReturnProjects_WhenOwnerExists() {
        // Arrange
        User mockOwner = new User();
        mockOwner.setId(1L);
        List<Project> mockProjects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findByOwner(mockOwner)).thenReturn(mockProjects);

        // Act
        List<Project> result = projectService.getProjectsByOwner(mockOwner);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findByOwner(mockOwner);
    }

    @Test
    void createProject_ShouldSaveAndReturnProject() {
        // Arrange
        Project mockProject = new Project();
        mockProject.setName("Test Project");
        when(projectRepository.save(mockProject)).thenReturn(mockProject);

        // Act
        Project result = projectService.createProject(mockProject);

        // Assert
        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        verify(projectRepository, times(1)).save(mockProject);
    }

    @Test
    void updateProject_ShouldUpdateAndReturnProject_WhenProjectExists() {
        // Arrange
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Old Project");
        Project updatedProject = new Project();
        updatedProject.setName("Updated Project");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);

        // Act
        Project result = projectService.updateProject(projectId, updatedProject);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Project", result.getName());
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    void updateProject_ShouldThrowException_WhenProjectDoesNotExist() {
        // Arrange
        Long projectId = 1L;
        Project updatedProject = new Project();
        updatedProject.setName("Updated Project");
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.updateProject(projectId, updatedProject));
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    void deleteProject_ShouldDeleteProject_WhenProjectExists() {
        // Arrange
        Long projectId = 1L;

        // Act
        projectService.deleteProject(projectId);

        // Assert
        verify(projectRepository, times(1)).deleteById(projectId);
    }
}
