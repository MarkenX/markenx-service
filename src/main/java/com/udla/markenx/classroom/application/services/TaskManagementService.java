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
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.domain.models.StudentTask;
import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentAssignmentJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.StudentTaskMapper;

@Service
public class TaskManagementService {

  private final TaskRepositoryPort taskRepository;
  private final StudentRepositoryPort studentRepository;
  private final StudentAssignmentJpaRepository studentAssignmentRepository;
  private final StudentTaskMapper studentTaskMapper;
  private final com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.TaskJpaRepository taskJpaRepository;
  private final com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentJpaRepository studentJpaRepository;

  public TaskManagementService(
      TaskRepositoryPort taskRepository,
      StudentRepositoryPort studentRepository,
      StudentAssignmentJpaRepository studentAssignmentRepository,
      StudentTaskMapper studentTaskMapper,
      com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.TaskJpaRepository taskJpaRepository,
      com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentJpaRepository studentJpaRepository) {
    this.taskRepository = taskRepository;
    this.studentRepository = studentRepository;
    this.studentAssignmentRepository = studentAssignmentRepository;
    this.studentTaskMapper = studentTaskMapper;
    this.taskJpaRepository = taskJpaRepository;
    this.studentJpaRepository = studentJpaRepository;
  }

  /**
   * Creates a new task for a course.
   * Automatically assigns the task to all students in the course.
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

    // Save task
    Task savedTask = taskRepository.save(task);

    // Assign task to all students in the course
    var students = studentRepository.findByCourseId(request.getCourseId());

    // Get the persisted task entity
    var taskEntity = taskJpaRepository.findAll().stream()
        .filter(t -> t.getExternalReference() != null &&
            t.getExternalReference().getPublicId().equals(savedTask.getId()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Task entity not found after save"));

    for (var student : students) {
      // Get the persisted student entity
      var studentEntity = studentJpaRepository.findAll().stream()
          .filter(s -> s.getExternalReference() != null &&
              s.getExternalReference().getPublicId().equals(student.getId()))
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Student entity not found"));

      // Create domain StudentTask
      StudentTask studentTask = new StudentTask(
          savedTask,
          student.getId(),
          student.getSerialNumber(),
          createdBy);

      // Calculate correct assignment status
      studentTask.updateStatus();

      // Create StudentTaskJpaEntity with persisted entity references
      com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity studentTaskEntity = new com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity();
      studentTaskEntity.setAssignment(taskEntity);
      studentTaskEntity.setStudent(studentEntity);
      studentTaskEntity.setCurrentStatus(studentTask.getAssignmentStatus());
      studentTaskEntity.setCreatedBy(createdBy);
      studentTaskEntity.setCreatedAt(java.time.LocalDateTime.now());
      studentTaskEntity.setUpdatedAt(java.time.LocalDateTime.now());
      studentTaskEntity.setStatus(com.udla.markenx.shared.domain.valueobjects.EntityStatus.ENABLED);

      // Create external reference
      var externalRef = new com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.ExternalReferenceJpaEntity();
      externalRef.setPublicId(studentTask.getId());
      externalRef.setCode(studentTask.getCode());
      externalRef.setEntityType("StudentTask");
      studentTaskEntity.setExternalReference(externalRef);

      studentAssignmentRepository.save(studentTaskEntity);
    }

    return savedTask;
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

  @Transactional(readOnly = true)
  public Page<Task> getTasksByStatus(
      com.udla.markenx.shared.domain.valueobjects.EntityStatus status, Pageable pageable) {
    return taskRepository.findByStatus(status, pageable);
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
