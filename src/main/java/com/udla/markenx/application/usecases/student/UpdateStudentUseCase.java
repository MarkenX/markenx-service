package com.udla.markenx.application.usecases.student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.application.dtos.requests.UpdateStudentRequestDTO;
import com.udla.markenx.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.core.exceptions.DomainException;
import com.udla.markenx.core.models.Student;

/**
 * Use case for updating student information.
 */
@Service
public class UpdateStudentUseCase {

  private final StudentRepositoryPort studentRepository;

  public UpdateStudentUseCase(StudentRepositoryPort studentRepository) {
    this.studentRepository = studentRepository;
  }

  /**
   * Updates student information (firstName, lastName, email).
   * 
   * @param id      the student ID
   * @param request DTO containing updated student data
   * @return the updated student domain model
   * @throws StudentNotFoundException    if student not found
   * @throws EmailAlreadyExistsException if new email is already in use
   */
  @Transactional
  public Student execute(Long id, UpdateStudentRequestDTO request) {
    // Find existing student
    Student existingStudent = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

    // Check if email is being changed and if new email already exists
    if (!existingStudent.getEmail().equals(request.email())) {
      if (studentRepository.existsByEmail(request.email())) {
        throw new EmailAlreadyExistsException("Email already in use: " + request.email());
      }
    }

    // Create updated student (domain model handles validation)
    Student updatedStudent = new Student(
        id,
        request.firstName(),
        request.lastName(),
        request.email());

    // Save using the existing external auth ID (we don't change it)
    return studentRepository.update(updatedStudent);
  }

  public static class StudentNotFoundException extends DomainException {
    public StudentNotFoundException(String message) {
      super(message);
    }
  }

  public static class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String message) {
      super(message);
    }
  }
}
