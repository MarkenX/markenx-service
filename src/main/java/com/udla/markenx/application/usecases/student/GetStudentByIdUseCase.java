package com.udla.markenx.application.usecases.student;

import org.springframework.stereotype.Service;

import com.udla.markenx.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.core.exceptions.DomainException;
import com.udla.markenx.core.models.Student;

/**
 * Use case for retrieving a single student by ID.
 */
@Service
public class GetStudentByIdUseCase {

  private final StudentRepositoryPort studentRepository;

  public GetStudentByIdUseCase(StudentRepositoryPort studentRepository) {
    this.studentRepository = studentRepository;
  }

  /**
   * Retrieves a student by their ID.
   * 
   * @param id the student ID
   * @return the student domain model
   * @throws StudentNotFoundException if student not found
   */
  public Student execute(Long id) {
    return studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
  }

  public static class StudentNotFoundException extends DomainException {
    public StudentNotFoundException(String message) {
      super(message);
    }
  }
}
