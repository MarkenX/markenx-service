package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.classroom.domain.interfaces.Assignment;
import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AssignmentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;
import com.udla.markenx.shared.infrastructure.out.data.persistence.exception.DomainMappingException;
import com.udla.markenx.shared.infrastructure.out.data.persistence.exception.EntityMappingException;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.BaseMapper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.MapperValidator;

@Component
@RequiredArgsConstructor
public final class AssignmentMapper implements BaseMapper<Assignment, AssignmentJpaEntity> {

  private final TaskMapper taskMapper;
  private final MapperValidator validator;

  @Override
  public @NonNull Assignment toDomain(AssignmentJpaEntity entity) {
    validateEntity(entity);

    // Handle Hibernate proxy - get the actual class
    Class<?> entityClass = org.hibernate.Hibernate.getClass(entity);

    if (TaskJpaEntity.class.isAssignableFrom(entityClass)) {
      return taskMapper.toDomain((TaskJpaEntity) entity);
    }

    throw createUnrecognizedEntityTypeException(entityClass);
  }

  @Override
  public @NonNull AssignmentJpaEntity toEntity(Assignment domain) {
    return toEntity(domain, null);
  }

  public @NonNull AssignmentJpaEntity toEntity(Assignment domain, CourseJpaEntity parentCourse) {
    validateDomain(domain);

    if (domain instanceof Task task) {
      return taskMapper.toEntity(task, parentCourse);
    }

    throw createUnrecognizedDomainTypeException(domain.getClass());
  }

  private void validateEntity(AssignmentJpaEntity entity) {
    validator.validateEntityNotNull(entity, AssignmentJpaEntity.class);
  }

  private void validateDomain(Assignment domain) {
    validator.validateDomainNotNull(domain, Assignment.class);
  }

  private DomainMappingException createUnrecognizedEntityTypeException(Class<?> entityClass) {
    return DomainMappingException.invalidState(
        AssignmentJpaEntity.class,
        "Unrecognized entity type: " + entityClass.getSimpleName());
  }

  private EntityMappingException createUnrecognizedDomainTypeException(Class<?> domainClass) {
    return EntityMappingException.invalidState(
        Assignment.class,
        "Unrecognized domain type: " + domainClass.getSimpleName());
  }
}