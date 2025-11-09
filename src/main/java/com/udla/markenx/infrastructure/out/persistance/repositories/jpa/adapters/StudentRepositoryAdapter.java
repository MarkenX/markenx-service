package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.core.models.Student;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.StudentJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.StudentMapper;

import java.util.Optional;

/**
 * JPA adapter implementing student repository operations.
 * 
 * Converts between domain models and JPA entities for database persistence.
 * Manages the Keycloak user ID as a method parameter.
 * 
 * @see StudentRepositoryPort for the port interface
 * @see StudentJpaRepository for Spring Data JPA operations
 */
@Repository
public class StudentRepositoryAdapter implements StudentRepositoryPort {

  private final StudentJpaRepository jpaRepository;

  /**
   * Constructor injection for Spring Data JPA repository.
   * 
   * @param jpaRepository the Spring Data JPA repository for StudentJpaEntity
   */
  public StudentRepositoryAdapter(StudentJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  /**
   * Saves a student to the database with external authentication user reference.
   * 
   * @param student        the student domain model to save
   * @param externalAuthId the external authentication system user ID
   * @return the saved student domain model with generated ID
   */
  @Override
  public Student save(Student student, String externalAuthId) {
    StudentJpaEntity entity = StudentMapper.toEntity(student, externalAuthId);
    StudentJpaEntity savedEntity = jpaRepository.save(entity);
    return StudentMapper.toDomain(savedEntity);
  }

  /**
   * Updates an existing student.
   * 
   * @param student the student domain model with updated data
   * @return the updated student
   */
  @Override
  public Student update(Student student) {
    // Find existing entity to preserve externalAuthId
    StudentJpaEntity existingEntity = jpaRepository.findById(student.getId())
        .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + student.getId()));

    // Update with existing externalAuthId
    StudentJpaEntity entity = StudentMapper.toEntity(student, existingEntity.getKeycloakUserId());
    StudentJpaEntity savedEntity = jpaRepository.save(entity);
    return StudentMapper.toDomain(savedEntity);
  }

  /**
   * Finds a student by their ID.
   * 
   * @param id the student ID
   * @return Optional containing the student if found, empty otherwise
   */
  @Override
  public Optional<Student> findById(Long id) {
    return jpaRepository.findById(id)
        .map(StudentMapper::toDomain);
  }

  /**
   * Finds a student by their email address.
   * 
   * @param email the student email
   * @return Optional containing the student if found, empty otherwise
   */
  @Override
  public Optional<Student> findByEmail(String email) {
    return jpaRepository.findByEmail(email)
        .map(StudentMapper::toDomain);
  }

  /**
   * Finds all students with pagination.
   * 
   * @param pageable pagination parameters
   * @return page of students
   */
  @Override
  public Page<Student> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable)
        .map(StudentMapper::toDomain);
  }

  @Override
  public Page<Student> findByCourseId(Long courseId, Pageable pageable) {
    return jpaRepository.findByCourseId(courseId, pageable)
        .map(StudentMapper::toDomain);
  }

  /**
   * Checks if a student exists with the given email.
   * 
   * @param email the student email
   * @return true if student exists, false otherwise
   */
  @Override
  public boolean existsByEmail(String email) {
    return jpaRepository.existsByEmail(email);
  }

  /**
   * Finds the external authentication system ID for a student.
   * 
   * @param id the student ID
   * @return Optional containing the external auth ID if found
   */
  @Override
  public Optional<String> findExternalAuthIdById(Long id) {
    return jpaRepository.findById(id)
        .map(StudentJpaEntity::getKeycloakUserId);
  }

  /**
   * Deletes a student by their ID.
   * 
   * @param id the student ID to delete
   */
  @Override
  public void deleteById(Long id) {
    jpaRepository.deleteById(id);
  }
}
