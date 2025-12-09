package com.example.task_tracker.task.service;

import com.example.task_tracker.project.exception.ProjectNotFoundException;
import com.example.task_tracker.project.model.Project;
import com.example.task_tracker.project.repository.ProjectRepository;
import com.example.task_tracker.task.dto.TaskCreateRequest;
import com.example.task_tracker.task.dto.TaskResponse;
import com.example.task_tracker.task.dto.TaskUpdateRequest;
import com.example.task_tracker.task.exception.TaskNotFoundException;
import com.example.task_tracker.task.model.Task;
import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;
import com.example.task_tracker.task.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service implementation of {@link TaskService} providing the business logic for creating, updating,
 * retrieving, and deleting tasks. Delegates persistence operations to {@link TaskRepository}.
 */
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    /**
     * Creates a new {@code TaskServiceImpl} with the required repository dependencies.
     *
     * @param taskRepository the repository used for task persistence
     * @param projectRepository the repository used for project lookups
     */
    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskResponse createTask(TaskCreateRequest request) {
        Task task = new Task(
            request.getTitle(), request.getDescription(),
            request.getStatus() != null ? request.getStatus() : TaskStatus.OPEN,
            request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM,
            request.getDueDate());

        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(request.getProjectId()));
            task.setProject(project);
        }
        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return mapToResponse(task);
    }

    @Override
    public Page<TaskResponse> getTasks(
        Pageable pageable, TaskStatus status, TaskPriority priority, Long projectId)
    {
        Page<Task> page;

        if (projectId != null) {
            if (status != null && priority != null) {
                page = taskRepository
                    .findAllByProjectIdAndStatusAndPriority(projectId, status, priority, pageable);
            } else if (status != null) {
                page = taskRepository.findAllByProjectIdAndStatus(projectId, status, pageable);
            } else if (priority != null) {
                page = taskRepository.findAllByProjectIdAndPriority(projectId, priority, pageable);
            } else {
                page = taskRepository.findAllByProjectId(projectId, pageable);
            }
        } else {
            if (status != null && priority != null) {
                page = taskRepository.findAllByStatusAndPriority(status, priority, pageable);
            } else if (status != null) {
                page = taskRepository.findAllByStatus(status, pageable);
            } else if (priority != null) {
                page = taskRepository.findAllByPriority(priority, pageable);
            } else {
                page = taskRepository.findAll(pageable);
            }
        }
        return page.map(this::mapToResponse);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        task.setDueDate(request.getDueDate());

        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(request.getProjectId()));
            task.setProject(project);
        } else {
            task.setProject(null);
        }
        Task updated = taskRepository.save(task);
        return mapToResponse(updated);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Maps a {@link Task} entity to its corresponding {@link TaskResponse} DTO.
     *
     * @param task the task entity to convert
     * @return the mapped response DTO
     */
    private TaskResponse mapToResponse(Task task) {
        Long projectId = task.getProject() != null ? task.getProject().getId() : null;
        String projectName = task.getProject() != null ? task.getProject().getName() : null;
        return new TaskResponse(
            task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),
            task.getPriority(), task.getDueDate(), task.getCreatedAt(), task.getUpdatedAt(),
            projectId, projectName);
    }

}