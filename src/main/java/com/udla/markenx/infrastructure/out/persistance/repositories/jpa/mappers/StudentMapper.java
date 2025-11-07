package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.models.Student;
import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

/**
 * Mapper for converting between Student domain models and StudentJpaEntity.
 * 
 * Utility class with static methods only - cannot be instantiated.
 */
public final class StudentMapper {

  /**
   * Private constructor to prevent instantiation.
   * This is a utility class with only static methods.
   */
  private StudentMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  /**
   * Converts JPA entity to domain model.
   * 
   * @param entity the JPA entity from database
   * @return the Student domain model
   * @throws DomainMappingException if entity is null
   */
  public static @NonNull Student toDomain(StudentJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    // Handle entities with email (new format)
    if (entity.getEmail() != null) {
      return new Student(
          entity.getId(),
          entity.getFirstName(),
          entity.getLastName(),
          entity.getEmail());
    } else {
      // Handle entities without email (legacy format)
      return new Student(
          entity.getId(),
          entity.getFirstName(),
          entity.getLastName());
    }
  }

  /**
   * Converts domain model to JPA entity with external authentication user ID.
   * 
   * @param domain         the Student domain model to convert
   * @param externalAuthId the external authentication system user ID (can be
   *                       null)
   * @return the StudentJpaEntity ready for persistence
   * @throws EntityMappingException if domain is null
   */
  public static @NonNull StudentJpaEntity toEntity(Student domain, String externalAuthId) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    StudentJpaEntity entity = new StudentJpaEntity();

    // Set ID if present (for updates)
    if (domain.getId() != null) {
      entity.setId(domain.getId());
    }

    // Map business data from domain model
    entity.setFirstName(domain.getFirstName());
    entity.setLastName(domain.getLastName());
    entity.setEmail(domain.getEmail());
    entity.setKeycloakUserId(externalAuthId);

    return entity;
  }
}
