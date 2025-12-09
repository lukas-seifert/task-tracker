package com.example.task_tracker.project.service;

import java.util.List;

import com.example.task_tracker.project.dto.ProjectCreateRequest;
import com.example.task_tracker.project.dto.ProjectResponse;
import com.example.task_tracker.project.dto.ProjectUpdateRequest;

/**
 * Service interface defining operations for managing projects.
 */
public interface ProjectService {

    /**
     * Creates a new project.
     *
     * @param request the project creation payload
     * @return the created project
     */
    ProjectResponse createProject(ProjectCreateRequest request);

    /**
     * Retrieves a single project by its ID.
     *
     * @param id the project identifier
     * @return the matching project response
     */
    ProjectResponse getProjectById(Long id);

    /**
     * Retrieves all projects.
     *
     * @return a list of all existing projects
     */
    List<ProjectResponse> getAllProjects();

    /**
     * Updates an existing project.
     *
     * @param id      the project identifier
     * @param request the update payload
     * @return the updated project
     */
    ProjectResponse updateProject(Long id, ProjectUpdateRequest request);

    /**
     * Deletes a project by its ID.
     *
     * @param id the project identifier
     */
    void deleteProject(Long id);

}