package com.example.task_tracker.task.service;

import com.example.task_tracker.task.dto.TaskCreateRequest;
import com.example.task_tracker.task.dto.TaskUpdateRequest;
import com.example.task_tracker.task.dto.TaskResponse;
import com.example.task_tracker.task.exception.TaskNotFoundException;
import com.example.task_tracker.task.model.Task;
import com.example.task_tracker.task.model.TaskPriority;
import com.example.task_tracker.task.model.TaskStatus;
import com.example.task_tracker.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link TaskServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private static TaskCreateRequest createTaskCreateRequest() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Test Task");
        request.setDescription("Description");
        request.setStatus(TaskStatus.OPEN);
        request.setPriority(TaskPriority.HIGH);
        request.setDueDate(LocalDate.of(2025, 1, 1));
        return request;
    }

    @Test
    void testCreateTask() {
        // given
        TaskCreateRequest request = createTaskCreateRequest();
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.<Task>getArgument(0));

        // when
        TaskResponse response = taskService.createTask(request);

        // then
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());
        Task toSave = captor.getValue();

        assertThat(toSave.getTitle()).isEqualTo("Test Task");
        assertThat(response.title()).isEqualTo("Test Task");
        assertThat(response.priority()).isEqualTo(TaskPriority.HIGH);
    }

    @Test
    void testGetTasks() {
        // given
        Task task1 = new Task("T1", "D1", TaskStatus.OPEN, TaskPriority.LOW, null);
        Task task2 = new Task("T2", "D2", TaskStatus.DONE, TaskPriority.HIGH, null);

        Page<Task> page = new PageImpl<>(List.of(task1, task2));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(page);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<TaskResponse> result = taskService.getTasks(pageable, null, null);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).title()).isEqualTo("T1");
        assertThat(result.getContent().get(1).status()).isEqualTo(TaskStatus.DONE);

        verify(taskRepository).findAll(pageable);
    }

    @Test
    void testGetTaskByIdWhenFound() {
        // given
        Task task = new Task(
                "Test Task",
                "Desc",
                TaskStatus.OPEN,
                TaskPriority.MEDIUM,
                null
        );
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when
        TaskResponse response = taskService.getTaskById(1L);

        // then
        assertThat(response.title()).isEqualTo("Test Task");
        assertThat(response.status()).isEqualTo(TaskStatus.OPEN);
    }

    @Test
    void testGetTaskByIdWhenNotFound() {
        // given
        when(taskRepository.findById(42L)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> taskService.getTaskById(42L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("42");
    }

    @Test
    void testUpdateTask() {
        // given
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated");
        request.setDescription("Updated desc");

        // when/then
        assertThatThrownBy(() -> taskService.updateTask(99L, request))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void testDeleteTaskWhenExists() {
        // given
        when(taskRepository.existsById(1L)).thenReturn(true);

        // when
        taskService.deleteTask(1L);

        // then
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testDeleteTaskWhenNotExists() {
        // given
        when(taskRepository.existsById(1L)).thenReturn(false);

        // when/then
        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(TaskNotFoundException.class);
    }

}