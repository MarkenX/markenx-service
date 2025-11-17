
package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.core.models.StudentTask;
import com.udla.markenx.core.models.Task;
import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StudentTaskMapper {

  private final TaskMapper taskMapper;
  private final AttemptMapper attemptMapper;

  public @NonNull StudentTask toDomain(StudentTaskJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    List<Attempt> attempts = entity.getAttempts().stream()
        .map(attemptMapper::toDomain)
        .toList();

    Task task = null;
    if (entity.getAssignment() instanceof TaskJpaEntity t) {
      task = taskMapper.toDomain(t);
    }

    StudentTask studentTask = new StudentTask(
        entity.getExternalReference().getPublicId(),
        entity.getExternalReference().getCode(),
        entity.getStatus(),
        task,
        entity.getAssignment().getExternalReference().getPublicId(),
        entity.getAssignment().getId(),
        attempts,
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());

    return studentTask;
  }

  public @NonNull StudentTaskJpaEntity toEntity(StudentTask domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    TaskJpaEntity task = taskMapper.toEntity(domain.getAssignment());

    StudentTaskJpaEntity entity = new StudentTaskJpaEntity();
    entity.getExternalReference().setPublicId(domain.getId());
    entity.getExternalReference().setCode(domain.getCode());
    entity.setAssignment(task);
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());

    List<AttemptJpaEntity> attempts = domain.getAttempts().stream()
        .map(attemptMapper::toEntity)
        .peek(e -> e.setStudentTask(entity))
        .toList();
    entity.setAttempts(attempts);

    return entity;
  }
}
