package com.example.task_tracker.task.dto;

import java.time.LocalDate;

import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data transfer object used for creating a new task.
 * Represents the payload of the HTTP POST /api/tasks request.
 */
public class TaskCreateRequest {

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;

    /** @return the title of the new task */
    public String getTitle() {
        return title;
    }

    /** @param title the title of the new task */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @return an optional description of the new task */
    public String getDescription() {
        return description;
    }

    /** @param description optional detailed text for the task */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return the initial status of the task (can be {@code null}, default is applied in entity) */
    public TaskStatus getStatus() {
        return status;
    }

    /** @param status the initial status to assign to the task */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /** @return the initial priority of the task */
    public TaskPriority getPriority() {
        return priority;
    }

    /** @param priority the initial priority to assign */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    /** @return the due date assigned to the new task */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /** @param dueDate the due date of the new task */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}