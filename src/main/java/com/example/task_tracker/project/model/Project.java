package com.example.task_tracker.project.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * JPA entity representing a logical grouping of tasks.
 * <p>
 * A project can be used to organize tasks by topic, area of life, or application
 * (for example: "Task Tracker", "Master Thesis", "Household").
 */
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    /**
     * Optional display color (e.g. hex code or named color) for UI purposes.
     */
    @Column(length = 20)
    private String color;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * No-args constructor required by JPA.
     */
    public Project() {}

    /**
     * Creates a new {@code Project} instance with the given fields.
     *
     * @param name        the project name
     * @param description optional project description
     * @param color       optional display color
     */
    public Project(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    /**
     * Lifecycle callback executed before the entity is first persisted.
     * Initializes timestamps.
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Lifecycle callback executed before the entity is updated.
     * Refreshes the {@code updatedAt} timestamp.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /** @return the project ID */
    public Long getId() {
        return id;
    }

    /** @return the project name */
    public String getName() {
        return name;
    }

    /** @param name the new project name */
    public void setName(String name) {
        this.name = name;
    }

    /** @return an optional project description */
    public String getDescription() {
        return description;
    }

    /** @param description the new project description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return the optional display color for this project */
    public String getColor() {
        return color;
    }

    /** @param color the new display color */
    public void setColor(String color) {
        this.color = color;
    }

    /** @return the timestamp when the project was created */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** @return the timestamp of the last update */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}