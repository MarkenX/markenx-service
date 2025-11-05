package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.interfaces.Assignment;
import com.udla.markenx.core.models.Task;
import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.MappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AssignmentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

public final class AssignmentMapper {

  private AssignmentMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static @NonNull Assignment toDomain(AssignmentJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    if (entity instanceof TaskJpaEntity task) {
      return TaskMapper.toDomain(task);
    } else {
      throw new MappingException("Tipo de asignación no reconocido: " + entity.getClass());
    }
  }

  public static @NonNull AssignmentJpaEntity toEntity(Assignment domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    if (domain instanceof Task task) {
      return TaskMapper.toEntity(task);
    } else {
      throw new MappingException("Tipo de asignación no reconocido: " + domain.getClass());
    }
  }
}
