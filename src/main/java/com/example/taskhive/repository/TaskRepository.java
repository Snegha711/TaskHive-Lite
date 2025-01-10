package com.example.taskhive.repository;

import com.example.taskhive.entity.Task;
import com.example.taskhive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User assignedTo);
    List<Task> findByStatus(Task.Status status);
}
