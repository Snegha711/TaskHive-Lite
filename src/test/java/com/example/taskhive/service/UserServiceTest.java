package com.example.taskhive.service;

import com.example.taskhive.entity.User;
import com.example.taskhive.repository.UserRepository;
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

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");


        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_shouldReturnUser_whenExists() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_shouldReturnEmpty_whenNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserByUsername_shouldReturnUser_whenExists() {

        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));

        Optional<User> result = userService.getUserByUsername("user1");

        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUsername());
        verify(userRepository, times(1)).findByUsername("user1");
    }

    @Test
    void getUserByUsername_shouldReturnEmpty_whenNotExists() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUsername("user1");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername("user1");
    }

    @Test
    void createUser_shouldSaveAndReturnUser_whenValid() {

        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("user1");
        user2.setEmail("user1@example.com");
        user2.setPassword("password1");
        when(userRepository.existsByUsername("user1")).thenReturn(false);
        when(userRepository.existsByEmail("user1@example.com")).thenReturn(false);
        when(userRepository.save(user1)).thenReturn(user2);

        User result = userService.createUser(user1);

        assertNotNull(result.getId());
        assertEquals("user1", result.getUsername());
        verify(userRepository, times(1)).existsByUsername("user1");
        verify(userRepository, times(1)).existsByEmail("user1@example.com");
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void createUser_shouldThrowException_whenUsernameExists() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        when(userRepository.existsByUsername("user1")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user1));

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("user1");
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldThrowException_whenEmailExists() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");

        when(userRepository.existsByUsername("user1")).thenReturn(false);
        when(userRepository.existsByEmail("user1@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user1));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("user1");
        verify(userRepository, times(1)).existsByEmail("user1@example.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_shouldDeleteUser_whenExists() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
