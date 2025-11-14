package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Attempt;

import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class AttemptMapper {

  private final EntityManager entityManager;

  public @NonNull Attempt toDomain(AttemptJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    UUID studentTaskId = entity.getStudentAssignment().getPublicId();
    Long studentId = entity.getStudentAssignment().getStudent().getId();
    Long taskId = entity.getStudentAssignment().getAssignment().getId();

    double taskMinScoreToPass = -1;
    var assignment = entity.getStudentAssignment().getAssignment();
    if (assignment instanceof TaskJpaEntity taskEntity) {
      taskMinScoreToPass = taskEntity.getMinScoreToPass();
    }

    Attempt domain = new Attempt(
        entity.getPublicId(),
        entity.getCode(),
        entity.getId(),
        entity.getStatus(),
        studentTaskId,
        studentId,
        taskId,
        taskMinScoreToPass,
        entity.getScore(),
        entity.getDuration(),
        entity.getResult(),
        entity.getCurrentStatus(),
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());

    return domain;
  }

  public @NonNull AttemptJpaEntity toEntity(Attempt domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    AttemptJpaEntity entity = new AttemptJpaEntity();
    entity.setPublicId(domain.getId());
    entity.setCode(domain.getCode());
    entity.setId(domain.getSequence());
    entity.setStatus(domain.getStatus());
    entity.setScore(domain.getScore());
    entity.setDuration(domain.getTimeSpent());
    entity.setResult(domain.getResult());
    entity.setCurrentStatus(domain.getAttemptStatus());

    if (domain.getStudentTaskId() != null) {
      StudentTaskJpaEntity ref = entityManager.getReference(StudentTaskJpaEntity.class, domain.getStudentTaskId());
      entity.setStudentAssignment(ref);
    }

    return entity;
  }
}
