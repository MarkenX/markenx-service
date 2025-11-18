package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.classroom.application.dtos.mappers.CourseDtoMapper;
import com.udla.markenx.classroom.application.dtos.mappers.StudentMapper;
import com.udla.markenx.classroom.application.dtos.requests.UpdateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.CourseResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.CourseControllerPort;
import com.udla.markenx.classroom.application.services.CourseService;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.domain.models.Student;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

/**
 * REST controller for Course management.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/markenx/courses-legacy")
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class CourseController implements CourseControllerPort {

  private final CourseService courseService;

  public CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  // @Override
  // @PostMapping
  // public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody
  // CreateCourseRequestDTO request) {
  // Course created = courseService.createCourse(request.getAcademicPeriodId(),
  // request.getLabel());
  // CourseResponseDTO response = CourseDtoMapper.toResponseDto(created);
  // return ResponseEntity.status(HttpStatus.CREATED).body(response);
  // }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<CourseResponseDTO> updateCourse(
      @PathVariable @Positive(message = "El ID del curso debe ser positivo") Long id,
      @Valid @RequestBody UpdateCourseRequestDTO request) {

    // com.udla.markenx.core.models.Course updated = courseService.updateCourse(id,
    // request.getAcademicPeriodId(),
    // request.getLabel());
    // CourseResponseDTO response = CourseDtoMapper.toResponseDto(updated);
    // return ResponseEntity.ok(response);
    return ResponseEntity.ok(null);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCourse(
      @PathVariable @Positive(message = "El ID del curso debe ser positivo") Long id) {

    // courseService.deleteCourse(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  @GetMapping
  public ResponseEntity<Page<CourseResponseDTO>> getAllCourses(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) int size) {

    Page<Course> courses = courseService.getAllCourseIds(page, size);
    Page<CourseResponseDTO> response = courses.map(CourseDtoMapper::toResponseDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<CourseResponseDTO> getCourseById(
      @PathVariable @Positive(message = "El ID del curso debe ser positivo") Long id) {

    Course course = courseService.getCourseById(id);
    CourseResponseDTO response = CourseDtoMapper.toResponseDto(course);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}/students")
  public ResponseEntity<Page<StudentResponseDTO>> getStudentsByCourseId(
      @PathVariable @Positive(message = "El ID del curso debe ser positivo") Long id,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) int size) {

    Page<Student> students = courseService.getStudentsByCourseId(id, page, size);
    Page<StudentResponseDTO> response = students.map(StudentMapper::toDto);
    return ResponseEntity.ok(response);
  }
}
