package com.example.taskhive.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("password123");
        user.setEmail("user1@example.com");
        user.setRole(User.Role.USER);

        assertNotNull(user);
        assertEquals("user1", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("user1@example.com", user.getEmail());
        assertEquals(User.Role.USER, user.getRole());
    }

    @Test
    void testGrantedAuthorities() {
        User user = new User();
        user.setRole(User.Role.ADMIN);

        assertNotNull(user.getAuthorities());
        assertEquals(1, user.getAuthorities().size());
        assertEquals("ROLE_ADMIN", user.getAuthorities().iterator().next().getAuthority());
    }
}
