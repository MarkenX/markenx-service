package com.udla.markenx.application.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.udla.markenx.application.dtos.mappers.TaskMapper;
import com.udla.markenx.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.application.ports.out.persistance.repositories.TaskRepositoryPort;
import com.udla.markenx.core.valueobjects.RangeDate;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

/**
 * Service for course domain operations.
 * 
 * Handles course-related business logic including:
 * - Getting course assignments/tasks with filtering
 */
@Service
public class CourseService {

  private final TaskRepositoryPort taskRepository;

  public CourseService(TaskRepositoryPort taskRepository) {
    this.taskRepository = taskRepository;
  }

  /**
   * Retrieves tasks/assignments for a specific course with optional filters.
   * 
   * @param courseId  the course ID
   * @param startDate optional start date filter
   * @param endDate   optional end date filter
   * @param status    optional assignment status filter
   * @param page      page number (default 0)
   * @param size      page size (default 10)
   * @return Page of TaskResponseDTO
   */
  public Page<TaskResponseDTO> getCourseTasks(Long courseId, LocalDate startDate, LocalDate endDate,
      AssignmentStatus status, int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

    if (startDate != null || endDate != null) {
      RangeDate rangeDate = new RangeDate(startDate, endDate);
      if (status != null) {
        return taskRepository.getCourseTasksByDueDateAndStatus(courseId, rangeDate, status, pageable)
            .map(TaskMapper::toResponseDto);
      } else {
        return taskRepository.getCourseTasksByDueDate(courseId, rangeDate, pageable)
            .map(TaskMapper::toResponseDto);
      }
    } else if (status != null) {
      return taskRepository.getCourseTasksByStatus(courseId, status, pageable)
          .map(TaskMapper::toResponseDto);
    } else {
      return taskRepository.getTasksByCourseId(courseId, pageable)
          .map(TaskMapper::toResponseDto);
    }
  }
}
