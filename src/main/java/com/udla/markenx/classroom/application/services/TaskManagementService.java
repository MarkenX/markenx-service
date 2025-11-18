package com.udla.markenx.classroom.application.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.dtos.requests.CreateTaskRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateTaskRequestDTO;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.domain.models.Task;

@Service
public class TaskManagementService {

  private final TaskRepositoryPort taskRepository;
  private final CourseRepositoryPort courseRepository;

  public TaskManagementService(
      TaskRepositoryPort taskRepository,
      CourseRepositoryPort courseRepository) {
    this.taskRepository = taskRepository;
    this.courseRepository = courseRepository;
  }

  @Transactional
  public Task createTask(CreateTaskRequestDTO request) {
    Course course = courseRepository.findById(request.getCourseId())
        .orElseThrow(() -> new ResourceNotFoundException("Curso", request.getCourseId()));

    Task newTask = new Task(
        course.getAcademicTermId(),
        course.getSequence().intValue(),
        request.getTitle(),
        request.getSummary(),
        request.getDueDate(),
        request.getMaxAttempts(),
        request.getMinScoreToPass());

    return taskRepository.save(newTask);
  }

  @Transactional
  public Task updateTask(Long id, UpdateTaskRequestDTO request) {
    Task existing = taskRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));

    existing.setTitle(request.getTitle());
    existing.setSummary(request.getSummary());
    existing.setDueDate(request.getDueDate());
    existing.setMaxAttempts(request.getMaxAttempts());
    existing.setMinimumScoreToPass(request.getMinScoreToPass());
    existing.markUpdated();

    return taskRepository.update(existing);
  }

  @Transactional(readOnly = true)
  public Task getTaskById(Long id) {
    if (isAdmin()) {
      return taskRepository.findByIdIncludingDisabled(id)
          .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));
    }
    return taskRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));
  }

  @Transactional(readOnly = true)
  public Page<Task> getAllTasks(Pageable pageable) {
    if (isAdmin()) {
      return taskRepository.findAllIncludingDisabled(pageable);
    }
    return taskRepository.findAll(pageable);
  }

  @Transactional
  public void deleteTask(Long id) {
    Task task = taskRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarea", id));

    task.disable();
    task.markUpdated();
    taskRepository.update(task);
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }
}
