package com.example.task_tracker.task.repository;

import com.example.task_tracker.task.model.Task;
import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findAllByPriority(TaskPriority priority, Pageable pageable);

    Page<Task> findAllByStatusAndPriority(TaskStatus status, TaskPriority priority, Pageable pageable);

}