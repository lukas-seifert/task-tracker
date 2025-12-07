package com.example.task_tracker.task.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA entity representing a task in the system.
 * Stores title, description, status, priority, due date, and automatic timestamps
 * for creation and updates.
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskPriority priority = TaskPriority.MEDIUM;

    private LocalDate dueDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Protected no-args constructor required by JPA.
     */
    protected Task() {
    }

    /**
     * Creates a new {@code Task} instance with the given fields.
     *
     * @param title the task title
     * @param description optional task description
     * @param status the initial task status
     * @param priority the task priority level
     * @param dueDate optional due date
     */
    public Task(String title,
                String description,
                TaskStatus status,
                TaskPriority priority,
                LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    /**
     * Lifecycle callback executed before the entity is first persisted.
     * Initializes timestamps and ensures default values for status and priority.
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = TaskStatus.OPEN;
        }
        if (this.priority == null) {
            this.priority = TaskPriority.MEDIUM;
        }
    }

    /**
     * Lifecycle callback executed before the entity is updated.
     * Refreshes the {@code updatedAt} timestamp.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /** @return the task ID */
    public Long getId() {
        return id;
    }

    /** @return the task title */
    public String getTitle() {
        return title;
    }

    /** Sets the task title. */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @return the task description */
    public String getDescription() {
        return description;
    }

    /** Sets the task description. */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return the current task status */
    public TaskStatus getStatus() {
        return status;
    }

    /** Updates the task status. */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /** @return the task priority level */
    public TaskPriority getPriority() {
        return priority;
    }

    /** Updates the task priority. */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    /** @return the due date or {@code null} if none is set */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /** Sets the due date. */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /** @return the timestamp when the task was created */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @return the timestamp of the last update */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}