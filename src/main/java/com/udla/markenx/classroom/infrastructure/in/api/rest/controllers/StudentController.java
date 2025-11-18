package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.classroom.application.dtos.mappers.StudentMapper;
import com.udla.markenx.classroom.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.StudentControllerPort;
import com.udla.markenx.classroom.application.usecases.student.GetAllStudentsUseCase;
import com.udla.markenx.classroom.application.usecases.student.GetStudentByIdUseCase;
import com.udla.markenx.classroom.application.usecases.student.GetCurrentStudentProfileUseCase;
import com.udla.markenx.classroom.domain.models.Student;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/markenx/students")
@Validated
@Tag(name = "Students", description = "Student operations including profile management and task retrieval. Accessible by STUDENT and ADMIN roles.")
@SecurityRequirement(name = "bearerAuth")
public class StudentController implements StudentControllerPort {

  private final GetAllStudentsUseCase getAllStudentsUseCase;
  private final GetStudentByIdUseCase getStudentByIdUseCase;
  private final GetCurrentStudentProfileUseCase getCurrentStudentProfileUseCase;

  public StudentController(
      GetAllStudentsUseCase getAllStudentsUseCase,
      GetStudentByIdUseCase getStudentByIdUseCase,
      GetCurrentStudentProfileUseCase getCurrentStudentProfileUseCase) {
    this.getAllStudentsUseCase = getAllStudentsUseCase;
    this.getStudentByIdUseCase = getStudentByIdUseCase;
    this.getCurrentStudentProfileUseCase = getCurrentStudentProfileUseCase;
  }

  @Override
  @GetMapping("/me")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get current student profile", description = "Retrieves the profile of the currently authenticated student based on their JWT token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Profile retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Student profile not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<StudentResponseDTO> getCurrentProfile(Authentication authentication) {
    String email = authentication.getName();
    Student student = getCurrentStudentProfileUseCase.execute(email);
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

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get all students", description = "Retrieves a paginated list of active students. Only returns enabled students.", parameters = {
      @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
      @Parameter(name = "size", description = "Number of items per page", example = "10"),
      @Parameter(name = "sort", description = "Sorting criteria (e.g., 'firstName,asc' or 'email,asc')", example = "firstName,asc")
  })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(Pageable pageable) {
    Page<Student> students = getAllStudentsUseCase.execute(pageable);
    Page<StudentResponseDTO> response = students.map(StudentMapper::toDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get student by ID", description = "Retrieves a specific active student by their unique identifier.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Student not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<StudentResponseDTO> getStudentById(
      @Parameter(description = "Student ID", required = true) @PathVariable Long id) {
    Student student = getStudentByIdUseCase.execute(id);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

}