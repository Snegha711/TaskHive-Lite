package com.example.taskhive.controller;

import com.example.taskhive.entity.User;
import com.example.taskhive.exception.InvalidInputException;
import com.example.taskhive.service.UserService;
import com.example.taskhive.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Inject PasswordEncoder (BCryptPasswordEncoder)

    // Login endpoint to authenticate users
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginUser) {
        // Find the user from the database
        if (loginUser.getUsername() == null || loginUser.getUsername().isEmpty()) {
            throw new InvalidInputException("Username is required");
        }

        if (loginUser.getPassword() == null || loginUser.getPassword().isEmpty()) {
            throw new InvalidInputException("Password is required");
        }
        User user = userService.getUserByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate the password using BCryptPasswordEncoder
        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Authenticate the user if password matches
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token if authentication is successful
        String jwt = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok("Bearer " + jwt);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new InvalidInputException("Username is required");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InvalidInputException("Password is required");
        }
        // Encode the password before saving it
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Encrypt the password
        User newUser = userService.createUser(user);  // Save the user with the encoded password

        return ResponseEntity.ok("User registered successfully");
    }
}
