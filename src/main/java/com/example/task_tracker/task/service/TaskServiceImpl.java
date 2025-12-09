package com.example.task_tracker.task.service;

import com.example.task_tracker.task.dto.TaskCreateRequest;
import com.example.task_tracker.task.dto.TaskResponse;
import com.example.task_tracker.task.dto.TaskUpdateRequest;
import com.example.task_tracker.task.exception.TaskNotFoundException;
import com.example.task_tracker.task.model.Task;
import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;
import com.example.task_tracker.task.repository.TaskRepository;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service implementation of {@link TaskService} providing the business logic for creating, updating,
 * retrieving, and deleting tasks. Delegates persistence operations to {@link TaskRepository}.
 */
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    /**
     * Creates a new {@code TaskServiceImpl} with the required repository dependency.
     *
     * @param taskRepository the repository used for task persistence
     */
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse createTask(TaskCreateRequest request) {
        Task task = new Task(
                request.getTitle(),
                request.getDescription(),
                request.getStatus() != null ? request.getStatus() : TaskStatus.OPEN,
                request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM,
                request.getDueDate()
        );

        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return mapToResponse(task);
    }

    @Override
    public Page<TaskResponse> getTasks(Pageable pageable, TaskStatus status, TaskPriority priority) {
        Page<Task> page;

        if (status != null && priority != null) {
            page = taskRepository.findAllByStatusAndPriority(status, priority, pageable);
        } else if (status != null) {
            page = taskRepository.findAllByStatus(status, pageable);
        } else if (priority != null) {
            page = taskRepository.findAllByPriority(priority, pageable);
        } else {
            page = taskRepository.findAll(pageable);
        }
        return page.map(this::mapToResponse);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        task.setDueDate(request.getDueDate());

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
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

}