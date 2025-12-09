package com.example.task_tracker.task.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for the application.
 * Converts thrown exceptions into consistent API error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles cases where a requested task cannot be found.
     *
     * @param ex the thrown {@link TaskNotFoundException}
     * @param request the originating HTTP request
     * @return a 404 Not Found error response
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(
        TaskNotFoundException ex, HttpServletRequest request)
    {
        ErrorResponse body = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Handles validation errors triggered by invalid request bodies.
     *
     * @param ex the thrown {@link MethodArgumentNotValidException}
     * @param request the originating HTTP request
     * @return a 400 Bad Request response containing field-specific messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException ex, HttpServletRequest request)
    {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream().collect(
            Collectors.toMap(
                FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage,
                (msg1, msg2) -> msg1 // keep first if duplicate keys appear
            ));

        ErrorResponse body = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), "Validation failed", request.getRequestURI(),
            fieldErrors);

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Handles validation errors raised from constraint-annotated method parameters.
     *
     * @param ex the thrown {@link ConstraintViolationException}
     * @param request the originating HTTP request
     * @return a 400 Bad Request response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
        ConstraintViolationException ex, HttpServletRequest request)
    {
        ErrorResponse body = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI(), null);
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Fallback handler for any unexpected, uncaught exceptions.
     *
     * @param request the originating HTTP request
     * @return a 500 Internal Server Error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error", request.getRequestURI(),
            null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}