package com.example.task_tracker.task.repository;

import com.example.task_tracker.task.model.Task;
import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing and querying {@link Task} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and defines
 * additional filters based on status and priority.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Retrieves a paginated list of tasks filtered by status.
     *
     * @param status the status to filter by
     * @param pageable pagination and sorting information
     * @return a page of matching tasks
     */
    Page<Task> findAllByStatus(TaskStatus status, Pageable pageable);

    /**
     * Retrieves a paginated list of tasks filtered by priority.
     *
     * @param priority the priority to filter by
     * @param pageable pagination and sorting information
     * @return a page of matching tasks
     */
    Page<Task> findAllByPriority(TaskPriority priority, Pageable pageable);

    /**
     * Retrieves a paginated list of tasks filtered by both status and priority.
     *
     * @param status the status to filter by
     * @param priority the priority to filter by
     * @param pageable pagination and sorting information
     * @return a page of matching tasks
     */
    Page<Task> findAllByStatusAndPriority(TaskStatus status, TaskPriority priority, Pageable pageable);

}