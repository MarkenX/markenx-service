package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.udla.markenx.classroom.application.dtos.mappers.StudentMapper;
import com.udla.markenx.classroom.application.dtos.responses.BulkImportResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.StudentControllerPort;
import com.udla.markenx.classroom.application.services.StudentManagementService;
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

  private final StudentManagementService studentManagementService;

  public StudentController(StudentManagementService studentManagementService) {
    this.studentManagementService = studentManagementService;
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get all students", description = "Retrieves a paginated list of students. Admins see all students including disabled ones. "
      +
      "Valid sort properties: id, status, createdAt, updatedAt, firstName, lastName, email, identityNumber, studentCode. "
      +
      "Example: ?page=0&size=10&sort=lastName,asc&sort=firstName,asc")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Students retrieved successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
      @Parameter(hidden = true) Pageable pageable) {
    Page<Student> students = studentManagementService.getAllStudents(pageable);
    Page<StudentResponseDTO> response = students.map(StudentMapper::toDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get student by ID", description = "Retrieves a specific student by their unique identifier. Admins can see disabled students.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Student found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Student not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<StudentResponseDTO> getStudentById(
      @Parameter(description = "Student ID", required = true) @PathVariable UUID id) {
    Student student = studentManagementService.getStudentById(id);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a new student", description = "Creates a single student and registers them in Keycloak. Email must belong to @udla.edu.ec domain. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Student created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Invalid data or validation errors"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role"),
      @ApiResponse(responseCode = "409", description = "Student already exists")
  })
  public ResponseEntity<StudentResponseDTO> createStudent(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Student data", required = true) @org.springframework.web.bind.annotation.RequestBody com.udla.markenx.classroom.application.dtos.requests.CreateStudentRequestDTO request) {
    Student student = studentManagementService.createStudent(request);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete (disable) a student", description = "Soft deletes a student by setting status to DISABLED. Student record remains in database but is not visible to non-admins. Keycloak user is NOT deleted. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Student disabled successfully"),
      @ApiResponse(responseCode = "404", description = "Student not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
  })
  public ResponseEntity<Void> deleteStudent(
      @Parameter(description = "Student ID", required = true) @PathVariable UUID id) {
    studentManagementService.deleteStudent(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping(value = "/bulk-import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Bulk import students from CSV", description = "Imports multiple students from a CSV file. CSV must have header: firstName,lastName,email,enrollmentCode. All emails must belong to @udla.edu.ec domain. Import is transactional (all-or-nothing). Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Students imported successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BulkImportResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Invalid CSV format or validation errors"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
  })
  public ResponseEntity<BulkImportResponseDTO> bulkImportStudents(
      @Parameter(description = "CSV file with student data", required = true) @RequestParam("file") MultipartFile file) {
    BulkImportResponseDTO response = studentManagementService.bulkImportStudents(file);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @GetMapping("/bulk-import/template")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Download CSV template", description = "Downloads a CSV template file for bulk student import. Template includes header row with required columns.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Template downloaded successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
  })
  public ResponseEntity<byte[]> downloadBulkImportTemplate() {
    String template = "firstName,lastName,email,enrollmentCode\n" +
        "Juan,Pérez,juan.perez@udla.edu.ec,202301234\n" +
        "María,García,maria.garcia@udla.edu.ec,202301235\n";

    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=student-import-template.csv")
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(template.getBytes());
  }

}