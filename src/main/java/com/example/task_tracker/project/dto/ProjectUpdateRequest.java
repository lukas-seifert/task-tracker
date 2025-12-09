package com.example.task_tracker.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data transfer object used for updating an existing project.
 * Represents the payload of the HTTP PUT /api/projects/{id} request.
 */
public class ProjectUpdateRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 500)
    private String description;

    @Size(max = 20)
    private String color;

    /** @return the updated project name */
    public String getName() {
        return name;
    }

    /** @param name the new name for the project */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the updated project description */
    public String getDescription() {
        return description;
    }

    /** @param description the new description for the project */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return the updated display color */
    public String getColor() {
        return color;
    }

    /** @param color the new display color */
    public void setColor(String color) {
        this.color = color;
    }

}