package com.example.task_tracker.task.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.task_tracker.task.dto.TaskCreateRequest;
import com.example.task_tracker.task.dto.TaskResponse;
import com.example.task_tracker.task.dto.TaskUpdateRequest;
import com.example.task_tracker.task.exception.GlobalExceptionHandler;
import com.example.task_tracker.task.exception.TaskNotFoundException;
import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;
import com.example.task_tracker.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for {@link TaskController}.
 */
@WebMvcTest(TaskController.class)
@Import(GlobalExceptionHandler.class)
@SuppressWarnings("removal")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTask() throws Exception {
        // given
        TaskResponse response = new TaskResponse(
            1L, "New Task", "Desc", TaskStatus.OPEN, TaskPriority.MEDIUM, LocalDate.of(2025, 1, 1),
            LocalDateTime.now(), LocalDateTime.now(), null, null);

        Mockito.when(taskService.createTask(any(TaskCreateRequest.class))).thenReturn(response);

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("New Task");
        request.setDescription("Desc");
        request.setStatus(TaskStatus.OPEN);
        request.setPriority(TaskPriority.MEDIUM);

        // when/then
        mockMvc
            .perform(
                post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.title", is("New Task")));
    }

    @Test
    void testCreateTaskValidationError() throws Exception {
        // given
        String invalidJson = """
            {
              "description": "Task without title",
              "status": "OPEN",
              "priority": "MEDIUM"
            }
            """;

        // when/then
        mockMvc
            .perform(
                post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.validationErrors.title").exists());
    }

    @Test
    void testGetTasks() throws Exception {
        // given
        TaskResponse t1 = new TaskResponse(
            1L, "T1", "D1", TaskStatus.OPEN, TaskPriority.LOW, null, LocalDateTime.now(),
            LocalDateTime.now(), null, null);
        TaskResponse t2 = new TaskResponse(
            2L, "T2", "D2", TaskStatus.DONE, TaskPriority.HIGH, null, LocalDateTime.now(),
            LocalDateTime.now(), null, null);

        Page<TaskResponse> page = new PageImpl<>(List.of(t1, t2));
        Mockito.when(taskService.getTasks(any(Pageable.class), any(), any(), any()))
            .thenReturn(page);

        // when/then
        mockMvc.perform(get("/api/tasks").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].title", is("T1")))
            .andExpect(jsonPath("$.content[1].status", is("DONE")));
    }

    @Test
    void testGetTaskByIdWhenNotFound() throws Exception {
        // given
        long missingId = 42L;
        Mockito.when(taskService.getTaskById(missingId))
            .thenThrow(new TaskNotFoundException(missingId));

        // when/then
        mockMvc.perform(get("/api/tasks/{id}", missingId)).andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Task with id 42 not found"))
            .andExpect(jsonPath("$.path").value("/api/tasks/42"));
    }

    @Test
    void testUpdateTask() throws Exception {
        // given
        TaskResponse updated = new TaskResponse(
            1L, "Updated", "Updated desc", TaskStatus.IN_PROGRESS, TaskPriority.HIGH, null,
            LocalDateTime.now(), LocalDateTime.now(), null, null);

        Mockito.when(taskService.updateTask(eq(1L), any(TaskUpdateRequest.class)))
            .thenReturn(updated);

        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated");
        request.setDescription("Updated desc");
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPriority(TaskPriority.HIGH);

        // when/then
        mockMvc
            .perform(
                put("/api/tasks/1").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk()).andExpect(jsonPath("$.title", is("Updated")))
            .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }

    @Test
    void testDeleteTask() throws Exception {
        // when/then
        mockMvc.perform(delete("/api/tasks/1")).andExpect(status().isNoContent());
    }

}