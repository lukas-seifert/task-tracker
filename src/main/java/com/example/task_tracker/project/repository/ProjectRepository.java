package com.example.task_tracker.project.repository;

import com.example.task_tracker.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing and querying {@link Project} entities.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
}