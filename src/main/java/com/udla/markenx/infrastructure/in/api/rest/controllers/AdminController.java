package com.udla.markenx.infrastructure.in.api.rest.controllers;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.udla.markenx.application.dtos.mappers.StudentMapper;
import com.udla.markenx.application.dtos.requests.CreateStudentRequestDTO;
import com.udla.markenx.application.dtos.requests.UpdateStudentRequestDTO;
import com.udla.markenx.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.application.services.StudentManagementService;
import com.udla.markenx.core.models.Student;

/**
 * REST controller for administrative operations.
 * 
 * Handles admin-only functionality like managing students and game scenarios.
 * All endpoints require ADMIN role.
 * 
 * Uses StudentManagementService as a facade (DDD best practice).
 */
@RestController
@RequestMapping("/api/markenx/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private final StudentManagementService studentManagementService;

  public AdminController(StudentManagementService studentManagementService) {
    this.studentManagementService = studentManagementService;
  }

  /**
   * DEBUG: Test authentication and roles.
   * Temporary endpoint to verify token is working.
   */
  @GetMapping("/debug/auth")
  public ResponseEntity<String> debugAuth(Authentication authentication) {
    StringBuilder debug = new StringBuilder();
    debug.append("Authenticated: ").append(authentication.isAuthenticated()).append("\n");
    debug.append("Principal: ").append(authentication.getName()).append("\n");
    debug.append("Authorities: ").append(authentication.getAuthorities()).append("\n");
    debug.append("Details: ").append(authentication.getDetails()).append("\n");
    return ResponseEntity.ok(debug.toString());
  }

  /**
   * Creates a new student.
   * 
   * @param request DTO containing email, password, firstName, lastName
   * @return ResponseEntity with created student DTO and HTTP 201 status
   */
  @PostMapping("/students")
  public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody CreateStudentRequestDTO request) {
    Student student = studentManagementService.createStudent(request);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * Retrieves all students with pagination.
   * 
   * @param pageable pagination parameters (page, size, sort)
   * @return ResponseEntity with page of student DTOs
   */
  @GetMapping("/students")
  public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(Pageable pageable) {
    Page<Student> students = studentManagementService.getAllStudents(pageable);
    Page<StudentResponseDTO> response = students.map(StudentMapper::toDto);
    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves a single student by ID.
   * 
   * @param id the student ID
   * @return ResponseEntity with student DTO
   */
  @GetMapping("/students/{id}")
  public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
    Student student = studentManagementService.getStudentById(id);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  /**
   * Updates an existing student.
   * 
   * @param id      the student ID
   * @param request DTO containing updated firstName, lastName, email
   * @return ResponseEntity with updated student DTO
   */
  @PutMapping("/students/{id}")
  public ResponseEntity<StudentResponseDTO> updateStudent(
      @PathVariable Long id,
      @Valid @RequestBody UpdateStudentRequestDTO request) {
    Student student = studentManagementService.updateStudent(id, request);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  /**
   * Deletes a student.
   * 
   * @param id the student ID
   * @return ResponseEntity with HTTP 204 No Content
   */
  @DeleteMapping("/students/{id}")
  public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
    studentManagementService.deleteStudent(id);
    return ResponseEntity.noContent().build();
  }
}
