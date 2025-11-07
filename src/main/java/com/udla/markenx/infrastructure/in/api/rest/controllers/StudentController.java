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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.application.dtos.mappers.StudentMapper;
import com.udla.markenx.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.application.services.StudentManagementService;
import com.udla.markenx.application.services.StudentService;
import com.udla.markenx.core.models.Student;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

/**
 * REST controller for student operations.
 * 
 * Provides endpoints for students to access their data and assigned tasks.
 */
@RestController
@RequestMapping("/api/markenx/students")
@Validated
public class StudentController {

  private final StudentService studentService;
  private final StudentManagementService studentManagementService;

  public StudentController(
      StudentService studentService,
      StudentManagementService studentManagementService) {
    this.studentService = studentService;
    this.studentManagementService = studentManagementService;
  }

  /**
   * Retrieves the profile of the currently authenticated student.
   * 
   * @param authentication the authentication object from JWT token
   * @return ResponseEntity with student profile DTO
   */
  @GetMapping("/me")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<StudentResponseDTO> getCurrentProfile(Authentication authentication) {
    String email = authentication.getName();
    Student student = studentManagementService.getCurrentStudentProfile(email);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves tasks assigned to a specific student with filters.
   * 
   * @param studentId the student ID
   * @param status    optional assignment status filter
   * @param startDate optional start date filter
   * @param endDate   optional end date filter
   * @param page      page number (default 0)
   * @param size      page size (default 10)
   * @return ResponseEntity with page of task DTOs
   */
  @GetMapping("/{studentId}/tasks")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<Page<TaskResponseDTO>> getTasksByFilters(
      @PathVariable @Positive(message = "El ID del estudiante debe ser positivo") Long studentId,
      @RequestParam(required = false) AssignmentStatus status,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) int size) {

    Page<TaskResponseDTO> response = studentService.getStudentTasks(
        studentId, startDate, endDate, status, page, size);
    return ResponseEntity.ok(response);
  }
}