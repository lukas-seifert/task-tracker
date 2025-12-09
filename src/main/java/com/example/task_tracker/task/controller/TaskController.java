package com.example.task_tracker.task.controller;

import com.example.task_tracker.task.dto.TaskCreateRequest;
import com.example.task_tracker.task.dto.TaskResponse;
import com.example.task_tracker.task.dto.TaskUpdateRequest;
import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;
import com.example.task_tracker.task.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST controller exposing CRUD operations for tasks.
 * Handles request validation, pagination, and filtering and delegates
 * business logic to {@link TaskService}.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Creates a new {@code TaskController} with the required service dependency.
     *
     * @param taskService the service handling task operations
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Creates a new task.
     *
     * @param request the request payload containing task fields
     * @return the created task wrapped in a {@link ResponseEntity}
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request) {
        TaskResponse created = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a paginated list of tasks with optional filtering by status and priority.
     * <p>
     * Sorting can be customized via the {@code sort} query parameter.
     * By default, tasks are sorted by {@code createdAt} in ascending order.
     *
     * @param pageable pagination and sorting information
     * @param status optional filter for task status
     * @param priority optional filter for task priority
     * @param projectId optional filter for tasks belonging to a specific project
     * @return a {@link Page} of matching task responses
     */
    @GetMapping
    public Page<TaskResponse> getTasks(
        @PageableDefault(
            size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable,
        @RequestParam(required = false) TaskStatus status,
        @RequestParam(required = false) TaskPriority priority,
        @RequestParam(required = false) Long projectId)
    {
        return taskService.getTasks(pageable, status, priority, projectId);
    }

    /**
     * Retrieves a single task by its ID.
     *
     * @param id the task identifier
     * @return the matching task response
     */
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    /**
     * Updates an existing task by its ID.
     *
     * @param id the task identifier
     * @param request the updated task fields
     * @return the updated task response
     */
    @PutMapping("/{id}")
    public TaskResponse updateTask(
        @PathVariable Long id, @Valid @RequestBody TaskUpdateRequest request)
    {
        return taskService.updateTask(id, request);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the task identifier
     * @return an empty 204 No Content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}