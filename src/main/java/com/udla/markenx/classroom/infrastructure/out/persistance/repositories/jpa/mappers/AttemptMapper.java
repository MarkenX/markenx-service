package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.domain.models.Attempt;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.ExternalReferenceJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class AttemptMapper {

  public @NonNull Attempt toDomain(AttemptJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    UUID studentTaskId = entity.getStudentTask().getExternalReference().getPublicId();
    Long studentId = entity.getStudentTask().getStudent().getId();
    Long taskId = entity.getStudentTask().getAssignment().getId();

    double taskMinScoreToPass = -1;
    var assignment = entity.getStudentTask().getAssignment();
    if (assignment instanceof TaskJpaEntity taskEntity) {
      taskMinScoreToPass = taskEntity.getMinScoreToPass();
    }

    Attempt domain = new Attempt(
        entity.getExternalReference().getPublicId(),
        entity.getExternalReference().getCode(),
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

    ExternalReferenceJpaEntity ref = new ExternalReferenceJpaEntity();
    ref.setPublicId(domain.getId());
    ref.setCode(domain.getCode());
    ref.setEntityType("ATTEMPT");

    entity.setExternalReference(ref);
    entity.setStatus(domain.getStatus());
    entity.setScore(domain.getScore());
    entity.setDuration(domain.getTimeSpent());
    entity.setResult(domain.getResult());
    entity.setCurrentStatus(domain.getAttemptStatus());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());
    entity.setUpdatedBy(domain.getUpdatedBy());

    return entity;
  }
}
