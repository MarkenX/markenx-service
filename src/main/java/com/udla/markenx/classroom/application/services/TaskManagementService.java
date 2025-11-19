package com.udla.markenx.classroom.application.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.dtos.requests.CreateTaskRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateTaskRequestDTO;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.domain.models.Task;

@Service
public class TaskManagementService {

  private final TaskRepositoryPort taskRepository;

  public TaskManagementService(TaskRepositoryPort taskRepository) {
    this.taskRepository = taskRepository;
  }

  /**
   * Creates a new task for a course.
   * 
   * @param request CreateTaskRequestDTO with task details
   * @return Created task
   */
  @Transactional
  public Task createTask(CreateTaskRequestDTO request) {
    // Get authenticated user as creator
    String createdBy = getAuthenticatedUsername();

    // Create task domain object
    Task task = new Task(
        request.getCourseId(),
        request.getAcademicTermYear(),
        request.getTitle(),
        request.getSummary(),
        request.getDueDate(),
        request.getMaxAttempts(),
        request.getMinScoreToPass(),
        createdBy);

    // Save and return
    return taskRepository.save(task);
  }

  /**
   * Updates an existing task.
   * 
   * @param id      Task UUID
   * @param request UpdateTaskRequestDTO with updated details
   * @return Updated task
   */
  @Transactional
  public Task updateTask(UUID id, UpdateTaskRequestDTO request) {
    // Find existing task
    Task existingTask = taskRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));

    // Update fields
    existingTask.setTitle(request.getTitle());
    existingTask.setSummary(request.getSummary());
    existingTask.setDueDate(request.getDueDate());
    existingTask.setMaxAttempts(request.getMaxAttempts());
    existingTask.setMinimumScoreToPass(request.getMinScoreToPass());

    // Save and return
    return taskRepository.update(existingTask);
  }

  /**
   * Retrieves a task by ID.
   * Admins can see disabled tasks.
   * 
   * @param id Task UUID
   * @return Task
   */
  @Transactional(readOnly = true)
  public Task getTaskById(UUID id) {
    if (isAdmin()) {
      return taskRepository.findByIdIncludingDisabled(id)
          .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));
    }
    return taskRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));
  }

  /**
   * Retrieves all tasks with pagination.
   * Admins can see disabled tasks.
   * 
   * @param pageable Pagination parameters
   * @return Page of tasks
   */
  @Transactional(readOnly = true)
  public Page<Task> getAllTasks(Pageable pageable) {
    if (isAdmin()) {
      return taskRepository.findAllIncludingDisabled(pageable);
    }
    return taskRepository.findAll(pageable);
  }

  /**
   * Disables a task (soft delete).
   * Validates that no StudentTask dependencies exist.
   * 
   * @param id Task UUID
   */
  @Transactional
  public void disableTask(UUID id) {
    // Find existing task
    Task task = taskRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));

    // Validate no dependencies
    if (taskRepository.hasStudentTaskDependencies(id)) {
      throw new IllegalStateException(
          "No se puede deshabilitar la tarea porque tiene asignaciones de estudiantes asociadas");
    }

    // Disable task
    task.disable();

    // Save updated task
    taskRepository.update(task);
  }

  /**
   * Enables a previously disabled task.
   * 
   * @param id Task UUID
   */
  @Transactional
  public void enableTask(UUID id) {
    // Find existing task
    Task task = taskRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));

    // Enable task
    task.enable();

    // Save updated task
    taskRepository.update(task);
  }

  /**
   * Gets the authenticated username from security context.
   * 
   * @return Username or "system" if not authenticated
   */
  private String getAuthenticatedUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      return authentication.getName();
    }
    return "system";
  }

  /**
   * Checks if the current user has ADMIN role.
   * 
   * @return true if user is admin
   */
  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }
}
