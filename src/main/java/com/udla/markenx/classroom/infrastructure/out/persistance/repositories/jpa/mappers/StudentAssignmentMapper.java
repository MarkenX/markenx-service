package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.core.interfaces.Assignment;
import com.udla.markenx.classroom.core.interfaces.StudentAssignment;
import com.udla.markenx.classroom.core.models.StudentTask;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.MappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentAssignmentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class StudentAssignmentMapper {

  private final StudentTaskMapper studentTaskMapper;

  public @NonNull StudentAssignment<?> toDomain(StudentAssignmentJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    if (entity instanceof StudentTaskJpaEntity studentTask) {
      return studentTaskMapper.toDomain(studentTask);
    } else {
      throw new MappingException("Tipo de asignación no reconocido: " + entity.getClass());
    }
  }

  public @NonNull <D extends Assignment> StudentAssignmentJpaEntity toEntity(StudentAssignment<D> domain,
      CourseJpaEntity parentCourse) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    if (domain instanceof StudentTask studentTask) {
      return studentTaskMapper.toEntity(studentTask, parentCourse);
    } else {
      throw new MappingException("Tipo de asignación no reconocido: " + domain.getClass());
    }
  }
}
