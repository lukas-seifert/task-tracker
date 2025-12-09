package com.example.task_tracker.project.dto;

import java.time.LocalDateTime;

/**
 * Response model exposing project data to API clients.
 */
public record ProjectResponse(Long id, String name, String description, String color,
    LocalDateTime createdAt, LocalDateTime updatedAt) {
}