package com.udla.markenx.classroom.application.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.dtos.requests.CreateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateCourseRequestDTO;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicTermRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.models.Course;

@Service
public class CourseManagementService {

  private final CourseRepositoryPort courseRepository;
  private final AcademicTermRepositoryPort academicPeriodRepository;

  public CourseManagementService(
      CourseRepositoryPort courseRepository,
      AcademicTermRepositoryPort academicPeriodRepository) {
    this.courseRepository = courseRepository;
    this.academicPeriodRepository = academicPeriodRepository;
  }

  @Transactional
  public Course createCourse(CreateCourseRequestDTO request) {
    java.util.UUID academicTermUUID = java.util.UUID.randomUUID();
    AcademicTerm academicTerm = academicPeriodRepository.findByIdIncludingDisabled(academicTermUUID)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", request.getAcademicPeriodId()));

    Course newCourse = new Course(
        academicTerm.getId(),
        academicTerm.getAcademicYear(),
        request.getLabel());

    return courseRepository.save(newCourse);
  }

  @Transactional
  public Course updateCourse(Long id, UpdateCourseRequestDTO request) {
    Course existing = courseRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Curso", id));

    existing.setName(request.getName());
    existing.markUpdated();

    return courseRepository.update(existing);
  }

  @Transactional(readOnly = true)
  public Course getCourseById(Long id) {
    if (isAdmin()) {
      return courseRepository.findByIdIncludingDisabled(id)
          .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
    }
    return courseRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
  }

  @Transactional(readOnly = true)
  public Page<Course> getAllCourses(Pageable pageable) {
    if (isAdmin()) {
      return courseRepository.findAllIncludingDisabled(pageable);
    }
    return courseRepository.findAll(pageable);
  }

  @Transactional
  public void deleteCourse(Long id) {
    Course course = courseRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Curso", id));

    course.disable();
    course.markUpdated();
    courseRepository.update(course);
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }
}
