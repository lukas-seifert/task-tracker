package com.example.task_tracker.task.exception;

/**
 * Exception thrown when a task with the specified ID cannot be found.
 * Used to signal missing resources during retrieval or update operations.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * Creates a new exception indicating that no task exists for the given ID.
     *
     * @param id the identifier of the missing task
     */
    public TaskNotFoundException(Long id) {
        super("Task with id " + id + " not found");
    }
    
}