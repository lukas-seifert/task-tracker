package com.example.task_tracker.task.repository;

import com.example.task_tracker.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {
    // -> standard methods like findAll, findById, save, deleteById
}