package com.udla.markenx.classroom.application.usecases.student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.shared.application.port.out.auth.AuthenticationServicePort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.DomainException;

/**
 * Use case for deleting a student from both database and authentication system.
 */
@Service
public class DeleteStudentUseCase {

  private final StudentRepositoryPort studentRepository;
  private final AuthenticationServicePort authenticationService;

  public DeleteStudentUseCase(
      StudentRepositoryPort studentRepository,
      AuthenticationServicePort authenticationService) {
    this.studentRepository = studentRepository;
    this.authenticationService = authenticationService;
  }

  @Transactional
  public void execute(Long id) {
    com.udla.markenx.classroom.domain.models.Student student = studentRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

    student.disable();
    student.markUpdated();
    studentRepository.update(student);

    String externalAuthId = studentRepository.findExternalAuthIdById(id).orElse(null);
    if (externalAuthId != null) {
      try {
        authenticationService.deleteUser(externalAuthId);
      } catch (Exception e) {
        System.err.println("Warning: Failed to delete user from authentication system: " + e.getMessage());
      }
    }
  }

  public static class StudentNotFoundException extends DomainException {
    public StudentNotFoundException(String message) {
      super(message);
    }
  }
}
