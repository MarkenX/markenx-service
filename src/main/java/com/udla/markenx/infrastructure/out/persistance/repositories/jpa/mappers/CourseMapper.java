package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.interfaces.Assignment;

import com.udla.markenx.core.models.Course;
import com.udla.markenx.core.models.Student;

import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AssignmentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

/**
 * Mapper for converting between Course domain models and CourseJpaEntity.
 * 
 * Handles conversion of Course objects and their nested collections
 * (students, tasks, assignments).
 * 
 * Utility class with static methods only - cannot be instantiated.
 */
public class CourseMapper {

  /**
   * Private constructor to prevent instantiation.
   * This is a utility class with only static methods.
   */
  private CourseMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  /**
   * Converts a CourseJpaEntity to a Course domain model.
   * Recursively converts all nested assignments and students.
   * 
   * @param entity the CourseJpaEntity from database
   * @return the Course domain model
   * @throws DomainMappingException if entity is null
   */
  public static @NonNull Course toDomain(CourseJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    // Convert all assignments from JPA entities to domain models
    List<Assignment> assignments = entity.getAssignments().stream()
        .map(AssignmentMapper::toDomain)
        .toList();

    // Convert all students from JPA entities to domain models
    List<Student> students = entity.getStudents().stream()
        .map(StudentMapper::toDomain)
        .toList();

    // Create domain model with all converted data
    Course domain = new Course(entity.getId(), assignments, students);

    return domain;
  }

  /**
   * Converts a Course domain model to a JPA entity.
   * Recursively converts all assignments and students.
   * 
   * @param domain the Course domain model
   * @return the CourseJpaEntity
   * @throws EntityMappingException if domain is null
   */
  public static @NonNull CourseJpaEntity toEntity(Course domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    CourseJpaEntity entity = new CourseJpaEntity();
    entity.setId(domain.getId());

    // Convert all assignments and set bidirectional relationship
    List<AssignmentJpaEntity> assignments = domain.getAssignments().stream()
        .map(AssignmentMapper::toEntity)
        .peek(e -> e.setCourse(entity))
        .toList();
    entity.setAssignments(assignments);

    // Convert all students and set bidirectional relationship
    // Note: Students from domain don't have Keycloak ID (pure domain model)
    // so we pass null for keycloakUserId. This is typically used for read
    // operations.
    List<StudentJpaEntity> students = domain.getStudents().stream()
        .map(student -> StudentMapper.toEntity(student, null))
        .peek(e -> e.setCourse(entity))
        .toList();
    entity.setStudents(students);

    return entity;
  }
}
