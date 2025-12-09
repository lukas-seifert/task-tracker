package com.example.task_tracker.task.service;

import com.example.task_tracker.task.dto.TaskCreateRequest;
import com.example.task_tracker.task.dto.TaskResponse;
import com.example.task_tracker.task.dto.TaskUpdateRequest;
import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface defining business operations for managing tasks.
 * Provides methods for creating, retrieving, updating, deleting,
 * and querying tasks with pagination and optional filters.
 */
public interface TaskService {

    /**
     * Creates a new task.
     *
     * @param request the input data for the new task
     * @return the created task as a response object
     */
    TaskResponse createTask(TaskCreateRequest request);

    /**
     * Retrieves a task by its ID.
     *
     * @param id the task identifier
     * @return the matching task response
     */
    TaskResponse getTaskById(Long id);

    /**
     * Returns a paginated list of tasks with optional filtering.
     * <p>
     * The underlying {@link Pageable} parameter supports pagination and sorting.
     * Sorting can be configured by passing a {@code sort} query parameter.
     *
     * @param pageable pagination and sorting information
     * @param status optional filter for task status (may be {@code null})
     * @param priority optional filter for task priority (may be {@code null})
     * @return a page of task responses
     */
    Page<TaskResponse> getTasks(Pageable pageable, TaskStatus status, TaskPriority priority);

    /**
     * Updates an existing task by its ID.
     *
     * @param id the task identifier
     * @param request the update payload
     * @return the updated task response
     */
    TaskResponse updateTask(Long id, TaskUpdateRequest request);

    /**
     * Deletes a task by its ID.
     *
     * @param id the task identifier
     */
    void deleteTask(Long id);

}