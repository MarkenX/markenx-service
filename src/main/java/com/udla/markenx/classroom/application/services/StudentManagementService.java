package com.udla.markenx.classroom.application.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.udla.markenx.classroom.application.dtos.requests.CreateStudentRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.BulkImportResponseDTO;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.domain.models.Student;

@Service
public class StudentManagementService {

  private final StudentRepositoryPort studentRepository;
  private final StudentService studentService;

  public StudentManagementService(
      StudentRepositoryPort studentRepository,
      StudentService studentService) {
    this.studentRepository = studentRepository;
    this.studentService = studentService;
  }

  @Transactional(readOnly = true)
  public Student getStudentById(UUID id) {
    if (isAdmin()) {
      return studentRepository.findByIdIncludingDisabled(id)
          .orElseThrow(() -> new ResourceNotFoundException("Estudiante", id));
    }
    return studentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Estudiante", id));
  }

  @Transactional(readOnly = true)
  public Page<Student> getAllStudents(Pageable pageable) {
    if (isAdmin()) {
      return studentRepository.findAllIncludingDisabled(pageable);
    }
    return studentRepository.findAll(pageable);
  }

  @Transactional(readOnly = true)
  public Student getCurrentStudentProfile(String email) {
    return studentRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Estudiante con email", email));
  }

  @Transactional
  public BulkImportResponseDTO bulkImportStudents(MultipartFile file) {
    return studentService.importStudentsFromCsv(file);
  }

  @Transactional
  public Student createStudent(CreateStudentRequestDTO request) {
    return studentService.createStudent(
        request.firstName(),
        request.lastName(),
        request.email(),
        request.password());
  }

  @Transactional
  public void deleteStudent(UUID id) {
    studentService.deleteStudent(id);
  }

  // TODO: Implement when Student domain supports update
  // @Transactional
  // public Student updateStudent(UUID id, UpdateStudentRequestDTO request) {
  // // Validate email doesn't exist
  // if (studentRepository.existsByEmail(request.getEmail())) {
  // throw new DuplicateResourceException("Ya existe un estudiante con el email: "
  // + request.getEmail());
  // }
  //
  // Student newStudent = new Student(
  // request.getFirstName(),
  // request.getLastName(),
  // request.getEmail(),
  // request.getEnrollmentCode());
  //
  // return studentRepository.save(newStudent);
  // }

  // @Transactional
  // public Student updateStudent(UUID id, UpdateStudentRequestDTO request) {
  // Student existing = studentRepository.findByIdIncludingDisabled(id)
  // .orElseThrow(() -> new ResourceNotFoundException("Estudiante", id));
  //
  // existing.setFirstName(request.getFirstName());
  // existing.setLastName(request.getLastName());
  // existing.markUpdated();
  //
  // return studentRepository.update(existing);
  // }

  // @Transactional
  // public void deleteStudent(UUID id) {
  // Student student = studentRepository.findByIdIncludingDisabled(id)
  // .orElseThrow(() -> new ResourceNotFoundException("Estudiante", id));
  //
  // student.disable();
  // student.markUpdated();
  // studentRepository.update(student);
  // }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }
}
