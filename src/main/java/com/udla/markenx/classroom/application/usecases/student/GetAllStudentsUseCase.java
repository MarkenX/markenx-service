package com.udla.markenx.classroom.application.usecases.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.domain.models.Student;

/**
 * Use case for retrieving all students with pagination.
 */
@Service
public class GetAllStudentsUseCase {

  private final StudentRepositoryPort studentRepository;

  public GetAllStudentsUseCase(StudentRepositoryPort studentRepository) {
    this.studentRepository = studentRepository;
  }

  /**
   * Retrieves all students with pagination support.
   * 
   * @param pageable pagination parameters (page number, size, sort)
   * @return page of students
   */
  public Page<Student> execute(Pageable pageable) {
    return studentRepository.findAll(pageable);
  }
}
