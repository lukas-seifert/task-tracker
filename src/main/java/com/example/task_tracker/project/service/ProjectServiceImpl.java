package com.example.task_tracker.project.service;

import java.util.List;

import com.example.task_tracker.project.dto.ProjectCreateRequest;
import com.example.task_tracker.project.dto.ProjectResponse;
import com.example.task_tracker.project.dto.ProjectUpdateRequest;
import com.example.task_tracker.project.exception.ProjectNotFoundException;
import com.example.task_tracker.project.model.Project;
import com.example.task_tracker.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link ProjectService} delegating persistence
 * operations to {@link ProjectRepository}.
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    /**
     * Creates a new {@code ProjectServiceImpl} with the given repository.
     *
     * @param projectRepository the repository used for project persistence
     */
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectResponse createProject(ProjectCreateRequest request) {
        Project project =
            new Project(request.getName(), request.getDescription(), request.getColor());

        Project saved = projectRepository.save(project);
        return mapToResponse(saved);
    }

    @Override
    public ProjectResponse getProjectById(Long id) {
        Project project =
            projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));
        return mapToResponse(project);
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectUpdateRequest request) {
        Project project =
            projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException(id));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setColor(request.getColor());

        Project updated = projectRepository.save(project);
        return mapToResponse(updated);
    }

    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        projectRepository.deleteById(id);
    }

    /**
     * Maps a {@link Project} entity to its {@link ProjectResponse} DTO.
     *
     * @param project the project entity to convert
     * @return the mapped response
     */
    private ProjectResponse mapToResponse(Project project) {
        return new ProjectResponse(
            project.getId(), project.getName(), project.getDescription(), project.getColor(),
            project.getCreatedAt(), project.getUpdatedAt());
    }

}