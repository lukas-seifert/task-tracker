package com.example.task_tracker.task.service;

import com.example.task_tracker.task.dto.TaskCreateRequest;
import com.example.task_tracker.task.dto.TaskResponse;
import com.example.task_tracker.task.dto.TaskUpdateRequest;
import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    TaskResponse createTask(TaskCreateRequest request);

    TaskResponse getTaskById(Long id);

    Page<TaskResponse> getTasks(Pageable pageable, TaskStatus status, TaskPriority priority);

    TaskResponse updateTask(Long id, TaskUpdateRequest request);

    void deleteTask(Long id);

}