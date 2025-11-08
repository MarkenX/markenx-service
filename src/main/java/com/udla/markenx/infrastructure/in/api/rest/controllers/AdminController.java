package com.udla.markenx.infrastructure.in.api.rest.controllers;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.udla.markenx.application.dtos.mappers.StudentMapper;
import com.udla.markenx.application.dtos.requests.CreateStudentRequestDTO;
import com.udla.markenx.application.dtos.requests.UpdateStudentRequestDTO;
import com.udla.markenx.application.dtos.responses.BulkImportResponseDTO;
import com.udla.markenx.application.dtos.responses.StudentResponseDTO;
import com.udla.markenx.application.ports.in.api.rest.controllers.AdminControllerPort;
import com.udla.markenx.application.services.StudentManagementService;
import com.udla.markenx.application.services.StudentService;
import com.udla.markenx.core.models.Student;

@RestController
@RequestMapping("/api/markenx/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController implements AdminControllerPort {

  private final StudentManagementService studentManagementService;
  private final StudentService studentService;

  public AdminController(
      StudentManagementService studentManagementService,
      StudentService studentService) {
    this.studentManagementService = studentManagementService;
    this.studentService = studentService;
  }

  @Override
  @GetMapping("/debug/auth")
  public ResponseEntity<String> debugAuth(Authentication authentication) {
    StringBuilder debug = new StringBuilder();
    debug.append("Authenticated: ").append(authentication.isAuthenticated()).append("\n");
    debug.append("Principal: ").append(authentication.getName()).append("\n");
    debug.append("Authorities: ").append(authentication.getAuthorities()).append("\n");
    debug.append("Details: ").append(authentication.getDetails()).append("\n");
    return ResponseEntity.ok(debug.toString());
  }

  @Override
  @PostMapping("/students")
  public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody CreateStudentRequestDTO request) {
    Student student = studentManagementService.createStudent(request);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @GetMapping("/students")
  public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(Pageable pageable) {
    Page<Student> students = studentManagementService.getAllStudents(pageable);
    Page<StudentResponseDTO> response = students.map(StudentMapper::toDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/students/{id}")
  public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
    Student student = studentManagementService.getStudentById(id);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  @Override
  @PutMapping("/students/{id}")
  public ResponseEntity<StudentResponseDTO> updateStudent(
      @PathVariable Long id,
      @Valid @RequestBody UpdateStudentRequestDTO request) {
    Student student = studentManagementService.updateStudent(id, request);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  @Override
  @DeleteMapping("/students/{id}")
  public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
    studentManagementService.deleteStudent(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping(value = "/students/bulk-import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BulkImportResponseDTO> bulkImportStudents(
      @RequestParam("file") MultipartFile file) {
    BulkImportResponseDTO response = studentService.importStudentsFromCsv(file);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
