package com.udla.markenx.classroom.application.usecases.student;

import org.springframework.stereotype.Service;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.core.exceptions.DomainException;
import com.udla.markenx.classroom.core.models.Student;

/**
 * Use case for student to retrieve their own profile.
 */
@Service
public class GetCurrentStudentProfileUseCase {

  private final StudentRepositoryPort studentRepository;

  public GetCurrentStudentProfileUseCase(StudentRepositoryPort studentRepository) {
    this.studentRepository = studentRepository;
  }

  /**
   * Retrieves the profile of the currently authenticated student.
   * 
   * @param email the email from JWT token (authenticated user)
   * @return the student domain model
   * @throws StudentNotFoundException if student not found
   */
  public Student execute(String email) {
    return studentRepository.findByEmail(email)
        .orElseThrow(() -> new StudentNotFoundException("Student profile not found for email: " + email));
  }

  public static class StudentNotFoundException extends DomainException {
    public StudentNotFoundException(String message) {
      super(message);
    }
  }
}
