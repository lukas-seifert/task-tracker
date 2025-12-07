package com.example.task_tracker.task.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents a standardized API error response.
 * Used by the {@link GlobalExceptionHandler} to return structured error details.
 */
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String path;
    private final Map<String, String> validationErrors;

    /**
     * Creates a new error response with status, message, request path,
     * and optional validation error details.
     *
     * @param status the HTTP status code
     * @param error a human-readable error message
     * @param path the request path where the error occurred
     * @param validationErrors optional field-level validation errors (ca be {@code null})
     */
    public ErrorResponse(int status, String error, String path, Map<String, String> validationErrors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.path = path;
        this.validationErrors = validationErrors;
    }

    /** @return the timestamp when the error response was created */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /** @return the HTTP status code */
    public int getStatus() {
        return status;
    }

    /** @return the main error message */
    public String getError() {
        return error;
    }

    /** @return the request path that caused the error */
    public String getPath() {
        return path;
    }

    /** @return field-specific validation errors, or null if none */
    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

}