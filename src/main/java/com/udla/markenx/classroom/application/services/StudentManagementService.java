package com.udla.markenx.classroom.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.udla.markenx.classroom.application.dtos.requests.CreateStudentRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.AttemptResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.BulkImportResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentTaskWithDetailsResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentWithCourseResponseDTO;
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
  public Page<Student> getStudentsByStatus(
      com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus status, Pageable pageable) {
    return studentRepository.findByStatus(status, pageable);
  }

  @Transactional(readOnly = true)
  public Student getCurrentStudentProfile(String email) {
    return studentRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Estudiante con email", email));
  }

  @Transactional(readOnly = true)
  public StudentWithCourseResponseDTO getCurrentStudentProfile() {
    return studentService.getCurrentStudentProfile();
  }

  @Transactional(readOnly = true)
  public org.springframework.data.domain.Page<StudentTaskWithDetailsResponseDTO> getCurrentStudentTasks(
      com.udla.markenx.classroom.domain.valueobjects.enums.AssignmentStatus status,
      java.time.LocalDate startDate,
      java.time.LocalDate endDate,
      org.springframework.data.domain.Pageable pageable) {
    return studentService.getCurrentStudentTasks(status, startDate, endDate, pageable);
  }

  @Transactional(readOnly = true)
  public List<AttemptResponseDTO> getCurrentStudentTaskAttempts(UUID taskId) {
    return studentService.getCurrentStudentTaskAttempts(taskId);
  }

  @Transactional
  public BulkImportResponseDTO bulkImportStudents(UUID courseId, MultipartFile file) {
    return studentService.importStudentsFromCsv(courseId, file);
  }

  @Transactional
  public Student createStudent(CreateStudentRequestDTO request) {
    return studentService.createStudent(
        request.firstName(),
        request.lastName(),
        request.email(),
        request.password(),
        request.courseId());
  }

  @Transactional
  public void disableStudent(UUID id) {
    studentService.disableStudent(id);
  }

  @Transactional
  public void enableStudent(UUID id) {
    studentService.enableStudent(id);
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
  // request.getEmail());
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
