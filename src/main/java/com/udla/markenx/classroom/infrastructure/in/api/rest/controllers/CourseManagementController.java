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

import com.udla.markenx.classroom.application.dtos.mappers.CourseDtoMapper;
import com.udla.markenx.classroom.application.dtos.requests.CreateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.CourseResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.CourseManagementControllerPort;
import com.udla.markenx.classroom.application.services.CourseManagementService;
import com.udla.markenx.classroom.domain.models.Course;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/markenx/courses")
@Validated
public class CourseManagementController implements CourseManagementControllerPort {

  private final CourseManagementService courseService;

  public CourseManagementController(CourseManagementService courseService) {
    this.courseService = courseService;
  }

  @Override
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CreateCourseRequestDTO request) {
    Course course = courseService.createCourse(request);
    CourseResponseDTO response = CourseDtoMapper.toResponseDto(course);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CourseResponseDTO> updateCourse(
      @PathVariable Long id,
      @Valid @RequestBody UpdateCourseRequestDTO request) {
    Course course = courseService.updateCourse(id, request);
    CourseResponseDTO response = CourseDtoMapper.toResponseDto(course);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
    Course course = courseService.getCourseById(id);
    CourseResponseDTO response = CourseDtoMapper.toResponseDto(course);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<Page<CourseResponseDTO>> getAllCourses(Pageable pageable) {
    Page<Course> courses = courseService.getAllCourses(pageable);
    Page<CourseResponseDTO> response = courses.map(CourseDtoMapper::toResponseDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
    courseService.deleteCourse(id);
    return ResponseEntity.noContent().build();
  }
}
