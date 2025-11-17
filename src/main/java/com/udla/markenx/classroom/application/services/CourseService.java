package com.udla.markenx.classroom.application.services;

import java.time.LocalDate;
import java.util.UUID;

import com.udla.markenx.classroom.domain.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.udla.markenx.classroom.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicPeriodRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.domain.valueobjects.enums.AssignmentStatus;

/**
 * Service for course domain operations.
 * 
 * Handles course-related business logic including:
 * - Getting course assignments/tasks with filtering
 */
@Service
public class CourseService {

  private final TaskRepositoryPort taskRepository;
  private final CourseRepositoryPort courseRepository;
  private final StudentRepositoryPort studentRepository;
  private final AcademicPeriodRepositoryPort periodRepository;

  public CourseService(TaskRepositoryPort taskRepository,
      CourseRepositoryPort courseRepository,
      StudentRepositoryPort studentRepository,
      AcademicPeriodRepositoryPort periodRepository) {
    this.taskRepository = taskRepository;
    this.courseRepository = courseRepository;
    this.studentRepository = studentRepository;
    this.periodRepository = periodRepository;
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

    // Pageable pageable = PageRequest.of(page, size);

    // if (startDate != null || endDate != null) {
    // RangeDate rangeDate = new RangeDate(startDate, endDate);
    // if (status != null) {
    // return taskRepository.getCourseTasksByDueDateAndStatus(courseId, rangeDate,
    // status, pageable)
    // .map(TaskMapper::toResponseDto);
    // } else {
    // return taskRepository.getCourseTasksByDueDate(courseId, rangeDate, pageable)
    // .map(TaskMapper::toResponseDto);
    // }
    // } else if (status != null) {
    // return taskRepository.getCourseTasksByStatus(courseId, status, pageable)
    // .map(TaskMapper::toResponseDto);
    // } else {
    // return taskRepository.getTasksByCourseId(courseId, pageable)
    // .map(TaskMapper::toResponseDto);
    // }
    return null;
  }

  // ---- New course management methods ----

  public Course createCourse(UUID academicPeriodId, String label) {
    if (academicPeriodId != null && periodRepository.findById(academicPeriodId).isEmpty()) {
      throw new ResourceNotFoundException("Período académico", academicPeriodId);
    }

    // Course course = new Course();
    Course course = null;
    // // set academicPeriodId and label if provided by rebuilding domain object
    // if (academicPeriodId != null || label != null) {
    // course = new Course(null, course.getAssignments(), course.getStudents(),
    // academicPeriodId, label,
    // course.getAuditInfo());
    // }

    return courseRepository.save(course);
  }

  // public Course updateCourse(Long id, Long academicPeriodId, String label) {
  // Course existing = courseRepository.findById(id)
  // .orElseThrow(() -> new ResourceNotFoundException("Curso", id));

  // if (academicPeriodId != null &&
  // periodRepository.findById(academicPeriodId).isEmpty()) {
  // throw new ResourceNotFoundException("Período académico", academicPeriodId);
  // }

  // // Create updated course domain preserving assignments/students and
  // timestamps
  // // Course updated = new Course(existing.getId(), existing.getAssignments(),
  // // existing.getStudents(), academicPeriodId,
  // // label == null ? existing.getName() : label, existing.getAuditInfo());

  // Course updated = null;
  // return courseRepository.update(updated);
  // }

  // public void deleteCourse(Long id) {
  // Course course = courseRepository.findById(id)
  // .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));

  // if (!course.getAssignments().isEmpty() || !course.getStudents().isEmpty()) {
  // throw new IllegalStateException("Cannot delete course with assignments or
  // students");
  // }

  // courseRepository.deleteById(id);
  // }

  public Page<Course> getAllCourseIds(int page, int size) {
    return courseRepository.findAll(PageRequest.of(page, size));
  }

  public Course getCourseById(Long id) {
    return courseRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
  }

  public org.springframework.data.domain.Page<Student> getStudentsByCourseId(Long courseId,
                                                                             int page, int size) {
    return studentRepository.findByCourseId(courseId, PageRequest.of(page, size));
  }
}
