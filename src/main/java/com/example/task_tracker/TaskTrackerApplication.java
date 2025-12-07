package com.example.task_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Task Tracker application.
 * Boots the Spring context and starts the embedded web server.
 */
@SpringBootApplication
public class TaskTrackerApplication {

    /**
     * Launches the Task Tracker application.
     *
     * @param args runtime arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApplication.class, args);
    }

}