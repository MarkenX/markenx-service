package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.classroom.application.dtos.mappers.TaskMapper;
import com.udla.markenx.classroom.application.dtos.requests.CreateTaskRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateTaskRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.TaskManagementControllerPort;
import com.udla.markenx.classroom.application.services.TaskManagementService;
import com.udla.markenx.classroom.domain.models.Task;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/markenx/tasks")
@Validated
public class TaskManagementController implements TaskManagementControllerPort {

  private final TaskManagementService taskService;

  public TaskManagementController(TaskManagementService taskService) {
    this.taskService = taskService;
  }

  @Override
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskRequestDTO request) {
    Task task = taskService.createTask(request);
    TaskResponseDTO response = TaskMapper.toResponseDto(task);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<TaskResponseDTO> updateTask(
      @PathVariable Long id,
      @Valid @RequestBody UpdateTaskRequestDTO request) {
    Task task = taskService.updateTask(id, request);
    TaskResponseDTO response = TaskMapper.toResponseDto(task);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
    Task task = taskService.getTaskById(id);
    TaskResponseDTO response = TaskMapper.toResponseDto(task);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(Pageable pageable) {
    Page<Task> tasks = taskService.getAllTasks(pageable);
    Page<TaskResponseDTO> response = tasks.map(TaskMapper::toResponseDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }
}
