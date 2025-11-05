package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.models.Student;
import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

public final class StudentMapper {

  private StudentMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static @NonNull Student toDomain(StudentJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    Student domain = new Student(
        entity.getId(),
        entity.getFirstName(),
        entity.getLastName());

    return domain;
  }

  public static @NonNull StudentJpaEntity toEntity(Student domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    StudentJpaEntity entity = new StudentJpaEntity();
    entity.setId(domain.getId());
    entity.setFirstName(domain.getFirstName());
    entity.setLastName(domain.getLastName());

    return entity;
  }
}
