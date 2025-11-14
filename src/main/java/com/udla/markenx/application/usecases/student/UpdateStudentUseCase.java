package com.udla.markenx.application.usecases.student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.application.dtos.requests.UpdateStudentRequestDTO;
import com.udla.markenx.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.core.exceptions.DomainException;
import com.udla.markenx.core.models.Student;

@Service
public class UpdateStudentUseCase {

  private final StudentRepositoryPort studentRepository;

  public UpdateStudentUseCase(StudentRepositoryPort studentRepository) {
    this.studentRepository = studentRepository;
  }

  @Transactional
  public Student execute(Long id, UpdateStudentRequestDTO request) {
    Student existingStudent = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

    if (!existingStudent.getEmail().equals(request.email())) {
      if (studentRepository.existsByEmail(request.email())) {
        throw new EmailAlreadyExistsException("Email already in use: " + request.email());
      }
    }

    Student updatedStudent = new Student(
        null,
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
