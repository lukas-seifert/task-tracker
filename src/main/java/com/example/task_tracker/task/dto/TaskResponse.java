package com.example.task_tracker.task.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;

/**
 * Response model for returning task data to API clients.
 * Used for GET operations such as retrieving a single task or listing tasks.
 * <p>
 * This record provides a read-only view of a task, including metadata such
 * as creation and update timestamps.
 */
public record TaskResponse(Long id, String title, String description, TaskStatus status,
    TaskPriority priority, LocalDate dueDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
}