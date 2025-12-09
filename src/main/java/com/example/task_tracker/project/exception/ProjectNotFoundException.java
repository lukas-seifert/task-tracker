package com.example.task_tracker.project.exception;

/**
 * Exception thrown when a project with the specified ID cannot be found.
 * Used to signal missing resources during retrieval or update operations.
 */
public class ProjectNotFoundException extends RuntimeException {

    /**
     * Creates a new exception indicating that no project exists for the given ID.
     *
     * @param id the identifier of the missing project
     */
    public ProjectNotFoundException(Long id) {
        super("Project with id " + id + " not found");
    }

}