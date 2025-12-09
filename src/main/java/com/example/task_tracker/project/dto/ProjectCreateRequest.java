package com.example.task_tracker.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data transfer object used for creating a new project.
 * Represents the payload of the HTTP POST /api/projects request.
 */
public class ProjectCreateRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 500)
    private String description;

    @Size(max = 20)
    private String color;

    /** @return the project name */
    public String getName() {
        return name;
    }

    /** @param name the project name */
    public void setName(String name) {
        this.name = name;
    }

    /** @return an optional project description */
    public String getDescription() {
        return description;
    }

    /** @param description the project description */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return an optional display color */
    public String getColor() {
        return color;
    }

    /** @param color the display color to use in the UI */
    public void setColor(String color) {
        this.color = color;
    }

}