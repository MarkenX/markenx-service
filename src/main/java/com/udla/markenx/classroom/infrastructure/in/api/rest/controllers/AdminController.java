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

@RestController
@RequestMapping("/api/markenx/admin")
@PreAuthorize("hasRole('ADMIN')")
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
  public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(Pageable pageable) {
    Page<Student> students = getAllStudentsUseCase.execute(pageable);
    Page<StudentResponseDTO> response = students.map(StudentMapper::toDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/students/{id}")
  public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
    Student student = getStudentByIdUseCase.execute(id);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }

  @Override
  @PostMapping(value = "/students/bulk-import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BulkImportResponseDTO> bulkImportStudents(
      @RequestParam("file") MultipartFile file) {
    BulkImportResponseDTO response = studentService.importStudentsFromCsv(file);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
