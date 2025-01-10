package com.example.taskhive.controller;

import com.example.taskhive.entity.User;
import com.example.taskhive.exception.InvalidInputException;
import com.example.taskhive.exception.ResourceNotFoundException;
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

import static com.example.taskhive.entity.User.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        User user = new User();
        user.setId(2L);
        user.setUsername("John");
        user.setPassword("password1");
        user.setEmail("e@gmail.com");
        user.setRole(ADMIN);
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setUsername("John");
        createdUser.setPassword("password1");
        createdUser.setEmail("e@gmail.com");
        createdUser.setRole(ADMIN);
        List<User> users = Arrays.asList(user, createdUser);
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        User user = new User();
        user.setUsername("John");
        user.setPassword("password1");
        user.setEmail("e@gmail.com");
        user.setRole(ADMIN);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<User> response = userController.getUserById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(user.getUsername(), response.getBody().getUsername());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userController.getUserById(1L));
        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        // Arrange
        User user = new User();
        user.setUsername("John");
        user.setPassword("password1");
        user.setEmail("e@gmail.com");
        user.setRole(ADMIN);
        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setUsername("John");
        createdUser.setPassword("password1");
        createdUser.setEmail("e@gmail.com");
        createdUser.setRole(ADMIN);
        when(userService.createUser(user)).thenReturn(createdUser);

        // Act
        ResponseEntity<User> response = userController.createUser(user);

        // Assert
        assertNotNull(response);
        assertEquals(createdUser.getId(), response.getBody().getId());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameIsMissing() {
        // Arrange
        User user = new User();
        user.setPassword("password1");
        user.setEmail("e@gmail.com");
        user.setRole(ADMIN);
        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> userController.createUser(user));
        assertEquals("Username is required", exception.getMessage());
        verify(userService, never()).createUser(any());
    }

    @Test
    void createUser_ShouldThrowException_WhenPasswordIsMissing() {
        // Arrange

        User user = new User();
        user.setUsername("John");
        user.setEmail("e@gmail.com");
        user.setRole(ADMIN);

        // Act & Assert
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> userController.createUser(user));
        assertEquals("Password is required", exception.getMessage());
        verify(userService, never()).createUser(any());
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("John");
        user.setPassword("password1");
        user.setEmail("e@gmail.com");
        user.setRole(ADMIN);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userService).deleteUser(1L);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(1L);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userController.deleteUser(1L));
        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userService, never()).deleteUser(any());
    }
}
