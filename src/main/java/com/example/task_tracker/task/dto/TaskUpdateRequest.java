package com.example.task_tracker.task.dto;

import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Data transfer object used for updating an existing task.
 * Represents the payload of the HTTP PUT /api/tasks/{id} request.
 */
public class TaskUpdateRequest {

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;

    /** @return the updated task title */
    public String getTitle() {
        return title;
    }

    /** @param title new title for the task */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @return the updated description */
    public String getDescription() {
        return description;
    }

    /** @param description new description for the task */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return the updated task status */
    public TaskStatus getStatus() {
        return status;
    }

    /** @param status new status for the task */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /** @return the updated task priority */
    public TaskPriority getPriority() {
        return priority;
    }

    /** @param priority new priority for the task */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    /** @return the updated due date */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /** @param dueDate new due date for the task */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}