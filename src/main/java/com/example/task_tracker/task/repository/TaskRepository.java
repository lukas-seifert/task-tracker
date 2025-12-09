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
 * additional filters based on status, priority, and project.
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
    Page<Task> findAllByStatusAndPriority(
        TaskStatus status, TaskPriority priority, Pageable pageable);

    /**
     * Retrieves tasks for a specific project.
     *
     * @param projectId the project identifier
     * @param pageable pagination and sorting information
     * @return a page of tasks belonging to the given project
     */
    Page<Task> findAllByProjectId(Long projectId, Pageable pageable);

    /**
     * Retrieves all tasks that belong to the given project and are in the specified status.
     *
     * @param projectId the identifier of the project whose tasks should be retrieved
     * @param status the status that the returned tasks must match
     * @param pageable the pagination information including page number, size, and sorting
     * @return a pageable list of tasks filtered by project and status
     */
    Page<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status, Pageable pageable);

    /**
     * Retrieves all tasks that belong to the given project and have the specified priority.
     *
     * @param projectId the identifier of the project whose tasks should be retrieved
     * @param priority the priority that the returned tasks must have
     * @param pageable pagination parameters such as page index, page size, and sorting order
     * @return a pageable list of tasks filtered by project and priority
     */
    Page<Task> findAllByProjectIdAndPriority(
        Long projectId, TaskPriority priority, Pageable pageable);

    /**
     * Retrieves all tasks that belong to the given project and match the specified status
     * and priority values.
     *
     * @param projectId the identifier of the project whose tasks should be retrieved
     * @param status the status that the returned tasks must match
     * @param priority the priority that the returned tasks must have
     * @param pageable pagination parameters including limit, offset, and sorting configuration
     * @return a pageable list of tasks filtered by project, status, and priority
     */
    Page<Task> findAllByProjectIdAndStatusAndPriority(
        Long projectId, TaskStatus status, TaskPriority priority, Pageable pageable);

}