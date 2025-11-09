package com.udla.markenx.application.ports.out.persistance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.core.models.Student;

import java.util.Optional;

/**
 * Repository port for Student entity operations.
 */
public interface StudentRepositoryPort {

  /**
   * Saves a student with their external authentication system user ID.
   *
   * @param student        the student domain model
   * @param externalAuthId the external authentication system user ID
   * @return the saved student with generated ID
   */
  Student save(Student student, String externalAuthId);

  /**
   * Updates an existing student.
   *
   * @param student the student domain model with updated data
   * @return the updated student
   */
  Student update(Student student);

  /**
   * Finds a student by their ID.
   *
   * @param id the student ID
   * @return Optional containing the student if found
   */
  Optional<Student> findById(Long id);

  /**
   * Finds a student by their email.
   *
   * @param email the student email
   * @return Optional containing the student if found
   */
  Optional<Student> findByEmail(String email);

  /**
   * Finds all students with pagination.
   *
   * @param pageable pagination parameters
   * @return page of students
   */
  Page<Student> findAll(Pageable pageable);

  Page<Student> findByCourseId(Long courseId, Pageable pageable);

  /**
   * Checks if a student exists by email.
   *
   * @param email the student email
   * @return true if student exists, false otherwise
   */
  boolean existsByEmail(String email);

  /**
   * Finds the external authentication system ID for a student.
   *
   * @param id the student ID
   * @return Optional containing the external auth ID if found
   */
  Optional<String> findExternalAuthIdById(Long id);

  /**
   * Deletes a student by ID.
   *
   * @param id the student ID
   */
  void deleteById(Long id);
}
