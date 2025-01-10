package com.example.taskhive.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    void testProjectCreation() {
        // Create a project instance
        User owner = new User();
        owner.setId(1L);
        owner.setUsername("owner1");
        owner.setEmail("owner@example.com");

        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("This is a test project");
        project.setOwner(owner);

        assertNotNull(project);
        assertEquals("Test Project", project.getName());
        assertEquals("This is a test project", project.getDescription());
        assertEquals(owner, project.getOwner());
    }
}
