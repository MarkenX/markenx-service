package com.udla.markenx.infrastructure.in.api.rest.controllers;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.udla.markenx.application.dtos.mappers.StudentMapper;
import com.udla.markenx.application.dtos.requests.CreateStudentRequestDTO;
import com.udla.markenx.application.dtos.requests.UpdateStudentRequestDTO;
import com.udla.markenx.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.application.ports.in.api.rest.controllers.StudentControllerPort;
import com.udla.markenx.application.services.CourseService;
import com.udla.markenx.application.services.StudentManagementService;
import com.udla.markenx.core.exceptions.ResourceNotFoundException;
import com.udla.markenx.core.models.Student;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

@RestController
@RequestMapping("/api/markenx/students")
@Validated
public class StudentController implements StudentControllerPort {

  private final StudentManagementService studentManagementService;
  private final CourseService courseService;

  public StudentController(
      StudentManagementService studentManagementService,
      CourseService courseService) {
    this.studentManagementService = studentManagementService;
    this.courseService = courseService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody CreateStudentRequestDTO request) {
    Student student = studentManagementService.createStudent(request);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(response);
  }

  @Override
  @GetMapping("/me")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<StudentResponseDTO> getCurrentProfile(Authentication authentication) {
    String email = authentication.getName();
    Student student = studentManagementService.getCurrentStudentProfile(email);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  // @Override
  // @GetMapping("/{studentId}/tasks")
  // @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  // public ResponseEntity<Page<TaskResponseDTO>> getTasksByFilters(
  // @PathVariable @Positive(message = "El ID del estudiante debe ser positivo")
  // Long studentId,
  // @RequestParam(required = false) AssignmentStatus status,
  // @RequestParam(required = false) @DateTimeFormat(iso =
  // DateTimeFormat.ISO.DATE) LocalDate startDate,
  // @RequestParam(required = false) @DateTimeFormat(iso =
  // DateTimeFormat.ISO.DATE) LocalDate endDate,
  // @RequestParam(defaultValue = "0") @Min(0) int page,
  // @RequestParam(defaultValue = "10") @Min(1) int size) {

  // Student student = studentManagementService.getStudentById(studentId);

  // if (student.getCourseId() == null) {
  // throw new ResourceNotFoundException("Curso para el estudiante", studentId);
  // }

  // Page<TaskResponseDTO> response = courseService.getCourseTasks(
  // student.getCourseId(), startDate, endDate, status, page, size);

  // return ResponseEntity.ok(response);
  // }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
    Student student = studentManagementService.getStudentById(id);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<StudentResponseDTO> updateStudent(
      @PathVariable Long id,
      @Valid @RequestBody UpdateStudentRequestDTO request) {
    Student student = studentManagementService.updateStudent(id, request);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
    studentManagementService.deleteStudent(id);
    return ResponseEntity.noContent().build();
  }
}