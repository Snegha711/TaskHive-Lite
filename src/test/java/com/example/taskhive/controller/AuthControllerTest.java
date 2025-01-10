package com.example.taskhive.controller;

import com.example.taskhive.entity.User;
import com.example.taskhive.exception.InvalidInputException;
import com.example.taskhive.service.UserService;
import com.example.taskhive.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLoginSuccess() throws Exception {
        User loginUser = new User();
        loginUser.setUsername("testuser");
        loginUser.setPassword("password123");

        User foundUser = new User();
        foundUser.setUsername("testuser");
        foundUser.setPassword("password123");  // Pretend this is the encoded password

        // Mock the behavior of the UserService and PasswordEncoder
        when(userService.getUserByUsername("testuser")).thenReturn(java.util.Optional.of(foundUser));
        when(passwordEncoder.matches("password123", foundUser.getPassword())).thenReturn(true);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(mock(Authentication.class));
        when(jwtUtil.generateToken("testuser")).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"testuser\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bearer mock-jwt-token"));

        verify(userService).getUserByUsername("testuser");
        verify(passwordEncoder).matches("password123", foundUser.getPassword());
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtUtil).generateToken("testuser");
    }


}
