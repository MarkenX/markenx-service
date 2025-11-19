package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import java.util.UUID;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.udla.markenx.classroom.application.dtos.mappers.TaskMapper;
import com.udla.markenx.classroom.application.dtos.requests.CreateTaskRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateTaskRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.TaskControllerPort;
import com.udla.markenx.classroom.application.services.TaskManagementService;
import com.udla.markenx.classroom.domain.models.Task;

@RestController
@RequestMapping("/api/markenx/tasks")
@Validated
@Tag(name = "Tasks", description = "Task management operations. Allows creating, updating, retrieving, and deleting tasks. Requires ADMIN role for modifications.")
@SecurityRequirement(name = "bearerAuth")
public class TaskController implements TaskControllerPort {

  private final TaskManagementService taskManagementService;

  public TaskController(TaskManagementService taskManagementService) {
    this.taskManagementService = taskManagementService;
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get all tasks", description = "Retrieves a paginated list of all tasks. Admins can see disabled tasks. "
      +
      "Valid sort properties: id, code, title, dueDate, maxAttempts, minScoreToPass, createdAt, updatedAt. "
      +
      "Example: ?page=0&size=10&sort=dueDate,asc&sort=title,asc")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(
      @Parameter(hidden = true) Pageable pageable) {
    Page<Task> tasks = taskManagementService.getAllTasks(pageable);
    Page<TaskResponseDTO> response = tasks.map(TaskMapper::toResponseDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get task by ID", description = "Retrieves a specific task by its unique identifier. Admins can see disabled tasks.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Task not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<TaskResponseDTO> getTaskById(
      @Parameter(description = "Task ID (UUID)", required = true) @PathVariable UUID id) {
    Task task = taskManagementService.getTaskById(id);
    TaskResponseDTO response = TaskMapper.toResponseDto(task);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a new task", description = "Creates a new task for a course without student assignments. Requires ADMIN role. The task will be created as a general assignment that can later be assigned to students.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Task created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Invalid data or validation errors"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
  })
  public ResponseEntity<TaskResponseDTO> createTask(
      @Parameter(description = "Task data", required = true) @Valid @RequestBody CreateTaskRequestDTO request) {
    Task task = taskManagementService.createTask(request);
    TaskResponseDTO response = TaskMapper.toResponseDto(task);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update a task", description = "Updates an existing task. Requires ADMIN role. Note: courseId and academicTermYear cannot be changed after creation.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Task updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Invalid data or validation errors"),
      @ApiResponse(responseCode = "404", description = "Task not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
  })
  public ResponseEntity<TaskResponseDTO> updateTask(
      @Parameter(description = "Task ID (UUID)", required = true) @PathVariable UUID id,
      @Parameter(description = "Updated task data", required = true) @Valid @RequestBody UpdateTaskRequestDTO request) {
    Task task = taskManagementService.updateTask(id, request);
    TaskResponseDTO response = TaskMapper.toResponseDto(task);
    return ResponseEntity.ok(response);
  }

  @Override
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete a task", description = "Disables a task (soft delete). Requires ADMIN role. Task can only be deleted if it has no student assignments (StudentTask dependencies).")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
      @ApiResponse(responseCode = "400", description = "Cannot delete task with dependencies"),
      @ApiResponse(responseCode = "404", description = "Task not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
  })
  public ResponseEntity<Void> deleteTask(
      @Parameter(description = "Task ID (UUID)", required = true) @PathVariable UUID id) {
    taskManagementService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }
}
