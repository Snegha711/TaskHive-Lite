package com.example.taskhive.controller;

import com.example.taskhive.entity.User;
import com.example.taskhive.exception.InvalidInputException;
import com.example.taskhive.exception.ResourceNotFoundException;
import com.example.taskhive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        // If the user is not found, throw ResourceNotFoundException
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Validate the user data, e.g., required fields
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new InvalidInputException("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InvalidInputException("Password is required");
        }

        return ResponseEntity.ok(userService.createUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // If the user does not exist, throw ResourceNotFoundException
        if (!userService.getUserById(id).isPresent()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
