package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.classroom.domain.interfaces.Assignment;
import com.udla.markenx.classroom.domain.interfaces.StudentAssignment;
import com.udla.markenx.classroom.domain.models.StudentTask;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentAssignmentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;
import com.udla.markenx.shared.infrastructure.out.data.persistence.exception.DomainMappingException;
import com.udla.markenx.shared.infrastructure.out.data.persistence.exception.EntityMappingException;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.BaseMapper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.MapperValidator;

@Component
@RequiredArgsConstructor
public final class StudentAssignmentMapper implements BaseMapper<StudentAssignment<?>, StudentAssignmentJpaEntity> {

  private final StudentTaskMapper studentTaskMapper;
  private final MapperValidator validator;

  @Override
  public @NonNull StudentAssignment<?> toDomain(StudentAssignmentJpaEntity entity) {
    validateEntity(entity);

    // Handle Hibernate proxy - get the actual class
    Class<?> entityClass = org.hibernate.Hibernate.getClass(entity);

    if (StudentTaskJpaEntity.class.isAssignableFrom(entityClass)) {
      return studentTaskMapper.toDomain((StudentTaskJpaEntity) entity);
    }

    throw createUnrecognizedEntityTypeException(entityClass);
  }

  @Override
  public @NonNull StudentAssignmentJpaEntity toEntity(StudentAssignment<?> domain) {
    return toEntity(domain, null);
  }

  public @NonNull <D extends Assignment> StudentAssignmentJpaEntity toEntity(
      StudentAssignment<D> domain,
      CourseJpaEntity parentCourse) {
    validateDomain(domain);

    if (domain instanceof StudentTask studentTask) {
      return studentTaskMapper.toEntity(studentTask, parentCourse);
    }

    throw createUnrecognizedDomainTypeException(domain.getClass());
  }

  private void validateEntity(StudentAssignmentJpaEntity entity) {
    validator.validateEntityNotNull(entity, StudentAssignmentJpaEntity.class);
  }

  private void validateDomain(StudentAssignment<?> domain) {
    validator.validateDomainNotNull(domain, StudentAssignment.class);
  }

  private DomainMappingException createUnrecognizedEntityTypeException(Class<?> entityClass) {
    return DomainMappingException.invalidState(
        StudentAssignmentJpaEntity.class,
        "Unrecognized entity type: " + entityClass.getSimpleName());
  }

  private EntityMappingException createUnrecognizedDomainTypeException(Class<?> domainClass) {
    return EntityMappingException.invalidState(
        StudentAssignment.class,
        "Unrecognized domain type: " + domainClass.getSimpleName());
  }
}