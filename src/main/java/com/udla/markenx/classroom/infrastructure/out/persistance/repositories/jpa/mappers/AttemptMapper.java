package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.classroom.domain.models.Attempt;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.BaseMapper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.ExternalReferenceMapperHelper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.MapperValidator;

@Component
@RequiredArgsConstructor
public final class AttemptMapper implements BaseMapper<Attempt, AttemptJpaEntity> {

  private final MapperValidator validator;
  private final ExternalReferenceMapperHelper externalReferenceHelper;

  @Override
  public @NonNull Attempt toDomain(AttemptJpaEntity entity) {
    validateEntity(entity);

    return new Attempt(
        extractPublicId(entity),
        extractCode(entity),
        entity.getId(),
        entity.getStatus(),
        extractStudentTaskId(entity),
        extractStudentId(entity),
        extractTaskId(entity),
        extractTaskMinScoreToPass(entity),
        entity.getScore(),
        entity.getDuration(),
        entity.getResult(),
        entity.getCurrentStatus(),
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  @Override
  public @NonNull AttemptJpaEntity toEntity(Attempt domain) {
    validateDomain(domain);

    AttemptJpaEntity entity = new AttemptJpaEntity();

    mapExternalReference(domain, entity);
    mapBasicFields(domain, entity);

    return entity;
  }

  private void validateEntity(AttemptJpaEntity entity) {
    validator.validateEntityNotNull(entity, AttemptJpaEntity.class);
    validator.validateEntityField(entity.getExternalReference(), AttemptJpaEntity.class, "externalReference");
    validator.validateEntityField(entity.getStudentTask(), AttemptJpaEntity.class, "studentTask");
  }

  private void validateDomain(Attempt domain) {
    validator.validateDomainNotNull(domain, Attempt.class);
    validator.validateDomainField(domain.getId(), Attempt.class, "id");
  }

  private UUID extractPublicId(AttemptJpaEntity entity) {
    return externalReferenceHelper.extractPublicId(
        entity.getExternalReference(),
        AttemptJpaEntity.class);
  }

  private String extractCode(AttemptJpaEntity entity) {
    return externalReferenceHelper.extractCode(entity.getExternalReference());
  }

  private UUID extractStudentTaskId(AttemptJpaEntity entity) {
    if (entity.getStudentTask().getExternalReference() == null) {
      validator.validateEntityField(null, AttemptJpaEntity.class, "studentTask.externalReference");
    }
    return entity.getStudentTask().getExternalReference().getPublicId();
  }

  private Long extractStudentId(AttemptJpaEntity entity) {
    if (entity.getStudentTask().getStudent() == null) {
      validator.validateEntityField(null, AttemptJpaEntity.class, "studentTask.student");
    }
    return entity.getStudentTask().getStudent().getId();
  }

  private Long extractTaskId(AttemptJpaEntity entity) {
    if (entity.getStudentTask().getAssignment() == null) {
      validator.validateEntityField(null, AttemptJpaEntity.class, "studentTask.assignment");
    }
    return entity.getStudentTask().getAssignment().getId();
  }

  private double extractTaskMinScoreToPass(AttemptJpaEntity entity) {
    var assignment = entity.getStudentTask().getAssignment();
    if (assignment == null) {
      return 0.0;
    }

    // Handle Hibernate proxy - unproxy to get the real entity
    Object unproxied = org.hibernate.Hibernate.unproxy(assignment);

    if (unproxied instanceof TaskJpaEntity taskEntity) {
      Double minScore = taskEntity.getMinScoreToPass();
      if (minScore != null) {
        return minScore;
      }
    }

    // Default value if assignment is not loaded or not a Task
    return 0.0;
  }

  private void mapExternalReference(Attempt domain, AttemptJpaEntity entity) {
    entity.setExternalReference(
        externalReferenceHelper.createExternalReference(
            domain.getId(),
            domain.getCode(),
            "ATTEMPT"));
  }

  private void mapBasicFields(Attempt domain, AttemptJpaEntity entity) {
    entity.setStatus(domain.getStatus());
    entity.setScore(domain.getScore());
    entity.setDuration(domain.getTimeSpent());
    entity.setResult(domain.getResult());
    entity.setCurrentStatus(domain.getAttemptStatus());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());
    entity.setUpdatedBy(domain.getUpdatedBy());
  }
}