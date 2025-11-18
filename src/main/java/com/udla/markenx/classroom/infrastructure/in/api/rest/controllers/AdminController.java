package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.udla.markenx.classroom.application.dtos.mappers.StudentMapper;
import com.udla.markenx.classroom.application.dtos.responses.BulkImportResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.AdminControllerPort;
import com.udla.markenx.classroom.application.services.StudentService;
import com.udla.markenx.classroom.application.usecases.student.GetAllStudentsUseCase;
import com.udla.markenx.classroom.application.usecases.student.GetStudentByIdUseCase;
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
@RequestMapping("/api/markenx/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Administrative operations for managing students and system resources. Requires ADMIN role.")
@SecurityRequirement(name = "bearerAuth")
public class AdminController implements AdminControllerPort {

  private final GetAllStudentsUseCase getAllStudentsUseCase;
  private final GetStudentByIdUseCase getStudentByIdUseCase;
  private final StudentService studentService;

  public AdminController(
      GetAllStudentsUseCase getAllStudentsUseCase,
      GetStudentByIdUseCase getStudentByIdUseCase,
      StudentService studentService) {
    this.getAllStudentsUseCase = getAllStudentsUseCase;
    this.getStudentByIdUseCase = getStudentByIdUseCase;
    this.studentService = studentService;
  }

  @Override
  @GetMapping("/debug/auth")
  @Operation(summary = "Debug authentication", description = "Returns authentication details for debugging purposes. Shows principal, authorities, and other JWT token information.")
  @ApiResponse(responseCode = "200", description = "Authentication details retrieved successfully")
  public ResponseEntity<String> debugAuth(Authentication authentication) {
    StringBuilder debug = new StringBuilder();
    debug.append("Authenticated: ").append(authentication.isAuthenticated()).append("\n");
    debug.append("Principal: ").append(authentication.getName()).append("\n");
    debug.append("Authorities: ").append(authentication.getAuthorities()).append("\n");
    debug.append("Details: ").append(authentication.getDetails()).append("\n");
    return ResponseEntity.ok(debug.toString());
  }

  @Override
  @GetMapping("/students")
  @Operation(summary = "Get all students", description = "Retrieves a paginated list of all students including disabled ones. Supports sorting and pagination.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Students retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token"),
      @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
  })
  public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
      @Parameter(description = "Pagination parameters (page, size, sort)") Pageable pageable) {
    Page<Student> students = getAllStudentsUseCase.execute(pageable);
    Page<StudentResponseDTO> response = students.map(StudentMapper::toDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/students/{id}")
  @Operation(summary = "Get student by ID", description = "Retrieves a specific student by their unique identifier, including disabled students.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Student not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<StudentResponseDTO> getStudentById(
      @Parameter(description = "Student ID", required = true) @PathVariable Long id) {
    Student student = getStudentByIdUseCase.execute(id);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping(value = "/students/bulk-import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Bulk import students from CSV", description = "Imports multiple students from a CSV file. CSV must have header: firstName,lastName,email,enrollmentCode. All emails must belong to @udla.edu.ec domain. Import is transactional (all-or-nothing).")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Students imported successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BulkImportResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Invalid CSV format or validation errors"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<BulkImportResponseDTO> bulkImportStudents(
      @Parameter(description = "CSV file with student data", required = true) @RequestParam("file") MultipartFile file) {
    BulkImportResponseDTO response = studentService.importStudentsFromCsv(file);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
