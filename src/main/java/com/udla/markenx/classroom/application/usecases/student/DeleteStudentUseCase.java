package com.udla.markenx.classroom.application.usecases.student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.shared.domain.application.port.out.auth.AuthenticationServicePort;
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

  /**
   * Deletes a student from database and authentication system.
   * 
   * @param id the student ID
   * @throws StudentNotFoundException if student not found
   */
  @Transactional
  public void execute(Long id) {
    // Verify student exists and get auth ID
    String externalAuthId = studentRepository.findExternalAuthIdById(id)
        .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

    // Delete from database first
    studentRepository.deleteById(id);

    // Delete from authentication system
    try {
      authenticationService.deleteUser(externalAuthId);
    } catch (Exception e) {
      // Log error but don't fail - database record already deleted
      System.err.println("Warning: Failed to delete user from authentication system: " + e.getMessage());
    }
  }

  public static class StudentNotFoundException extends DomainException {
    public StudentNotFoundException(String message) {
      super(message);
    }
  }
}
