package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.models.Student;
import com.udla.markenx.core.models.StudentTask;
import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class StudentMapper {

  public static @NonNull Student toDomain(StudentJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    UUID courseId = null;
    if (entity.getCourse() != null) {
      courseId = entity.getCourse().getPublicId();
    }

    List<StudentTask> tasks = entity.get

    return new Student(
        entity.getPublicId(),
        entity.getCode(),
        entity.getId(),
        entity.getStatus(),
        courseId,
        entity.getFirstName(),
        entity.getLastName(),
        entity.getEmail(),
        tasks,
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  public static @NonNull StudentJpaEntity toEntity(Student domain, String externalAuthId) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    StudentJpaEntity entity = new StudentJpaEntity();

    if (domain.getId() != null) {
      entity.setId(domain.getId());
    }

    entity.setFirstName(domain.getFirstName());
    entity.setLastName(domain.getLastName());
    entity.setEmail(domain.getEmail());
    entity.setKeycloakUserId(externalAuthId);

    return entity;
  }
}
