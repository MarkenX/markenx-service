package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.core.interfaces.Assignment;
import com.udla.markenx.classroom.core.models.Task;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.MappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AssignmentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class AssignmentMapper {

  private final TaskMapper taskMapper;

  public @NonNull Assignment toDomain(AssignmentJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    if (entity instanceof TaskJpaEntity task) {
      return taskMapper.toDomain(task);
    } else {
      throw new MappingException("Tipo de asignación no reconocido: " + entity.getClass());
    }
  }

  public @NonNull AssignmentJpaEntity toEntity(Assignment domain, CourseJpaEntity parentCourse) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    if (domain instanceof Task task) {
      return taskMapper.toEntity(task, parentCourse);
    } else {
      throw new MappingException("Tipo de asignación no reconocido: " + domain.getClass());
    }
  }
}
