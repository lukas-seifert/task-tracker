package com.example.task_tracker.project.controller;

import java.util.List;

import com.example.task_tracker.project.dto.ProjectCreateRequest;
import com.example.task_tracker.project.dto.ProjectResponse;
import com.example.task_tracker.project.dto.ProjectUpdateRequest;
import com.example.task_tracker.project.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST controller exposing CRUD operations for projects.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Creates a new {@code ProjectController} with the required service dependency.
     *
     * @param projectService the service handling project operations
     */
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Creates a new project.
     *
     * @param request the project payload
     * @return the created project
     */
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
        @Valid @RequestBody ProjectCreateRequest request)
    {
        ProjectResponse created = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves all projects.
     *
     * @return a list of all projects
     */
    @GetMapping
    public List<ProjectResponse> getProjects() {
        return projectService.getAllProjects();
    }

    /**
     * Retrieves a single project by its ID.
     *
     * @param id the project identifier
     * @return the matching project
     */
    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    /**
     * Updates an existing project.
     *
     * @param id the project identifier
     * @param request the updated project fields
     * @return the updated project
     */
    @PutMapping("/{id}")
    public ProjectResponse updateProject(
        @PathVariable Long id, @Valid @RequestBody ProjectUpdateRequest request)
    {
        return projectService.updateProject(id, request);
    }

    /**
     * Deletes a project by its ID.
     *
     * @param id the project identifier
     * @return an empty 204 No Content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

}