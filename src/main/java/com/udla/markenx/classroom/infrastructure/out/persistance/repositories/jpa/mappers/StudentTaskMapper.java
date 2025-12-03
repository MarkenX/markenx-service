
package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.classroom.domain.models.Attempt;
import com.udla.markenx.classroom.domain.models.StudentTask;
import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.BaseMapper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.ExternalReferenceMapperHelper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.MapperValidator;

@Component
@RequiredArgsConstructor
public class StudentTaskMapper implements BaseMapper<StudentTask, StudentTaskJpaEntity> {

  private final TaskMapper taskMapper;
  private final AttemptMapper attemptMapper;
  private final MapperValidator validator;
  private final ExternalReferenceMapperHelper externalReferenceHelper;

  @Override
  public @NonNull StudentTask toDomain(StudentTaskJpaEntity entity) {
    validateEntity(entity);

    return new StudentTask(
        extractPublicId(entity),
        extractCode(entity),
        entity.getStatus(),
        extractTask(entity),
        extractAssignmentPublicId(entity),
        extractAssignmentSequence(entity),
        mapAttemptsToDomain(entity),
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  @Override
  public @NonNull StudentTaskJpaEntity toEntity(StudentTask domain) {
    return toEntity(domain, null);
  }

  public @NonNull StudentTaskJpaEntity toEntity(StudentTask domain, CourseJpaEntity parentCourse) {
    validateDomain(domain);

    StudentTaskJpaEntity entity = new StudentTaskJpaEntity();

    mapExternalReference(domain, entity);
    mapBasicFields(domain, entity);
    mapAssignment(domain, parentCourse, entity);
    mapAttempts(domain, entity);

    return entity;
  }

  private void validateEntity(StudentTaskJpaEntity entity) {
    validator.validateEntityNotNull(entity, StudentTaskJpaEntity.class);
    validator.validateEntityField(entity.getExternalReference(), StudentTaskJpaEntity.class, "externalReference");
    validator.validateEntityField(entity.getAssignment(), StudentTaskJpaEntity.class, "assignment");
    validator.validateEntityField(entity.getAttempts(), StudentTaskJpaEntity.class, "attempts");
  }

  private void validateDomain(StudentTask domain) {
    validator.validateDomainNotNull(domain, StudentTask.class);
    validator.validateDomainField(domain.getId(), StudentTask.class, "id");
    validator.validateDomainField(domain.getAssignment(), StudentTask.class, "assignment");
    validator.validateDomainField(domain.getAttempts(), StudentTask.class, "attempts");
  }

  private UUID extractPublicId(StudentTaskJpaEntity entity) {
    return externalReferenceHelper.extractPublicId(
        entity.getExternalReference(),
        StudentTaskJpaEntity.class);
  }

  private String extractCode(StudentTaskJpaEntity entity) {
    return externalReferenceHelper.extractCode(entity.getExternalReference());
  }

  private Task extractTask(StudentTaskJpaEntity entity) {
    var assignment = entity.getAssignment();
    if (assignment == null) {
      return null;
    }

    // Handle Hibernate proxy - unproxy to get the real entity
    Object unproxied = org.hibernate.Hibernate.unproxy(assignment);

    if (unproxied instanceof TaskJpaEntity taskEntity) {
      return taskMapper.toDomain(taskEntity);
    }
    return null;
  }

  private UUID extractAssignmentPublicId(StudentTaskJpaEntity entity) {
    return externalReferenceHelper.extractPublicId(
        entity.getAssignment().getExternalReference(),
        StudentTaskJpaEntity.class);
  }

  private Long extractAssignmentSequence(StudentTaskJpaEntity entity) {
    return entity.getAssignment().getId();
  }

  private List<Attempt> mapAttemptsToDomain(StudentTaskJpaEntity entity) {
    return entity.getAttempts().stream()
        .map(attemptMapper::toDomain)
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
  }

  private void mapExternalReference(StudentTask domain, StudentTaskJpaEntity entity) {
    entity.setExternalReference(
        externalReferenceHelper.createExternalReference(
            domain.getId(),
            domain.getCode(),
            "STUDENT_TASK"));
  }

  private void mapBasicFields(StudentTask domain, StudentTaskJpaEntity entity) {
    entity.setStatus(domain.getEntityStatus());
    entity.setCurrentStatus(domain.getAssignmentStatus());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());
    entity.setUpdatedBy(domain.getUpdatedBy());
  }

  private void mapAssignment(StudentTask domain, CourseJpaEntity parentCourse, StudentTaskJpaEntity entity) {
    TaskJpaEntity task = findExistingTask(domain, parentCourse);

    if (task == null) {
      task = taskMapper.toEntity(domain.getAssignment(), parentCourse);
    }

    entity.setAssignment(task);
  }

  private TaskJpaEntity findExistingTask(StudentTask domain, CourseJpaEntity parentCourse) {
    if (parentCourse == null || parentCourse.getAssignments() == null) {
      return null;
    }

    return parentCourse.getAssignments().stream()
        .filter(TaskJpaEntity.class::isInstance)
        .map(TaskJpaEntity.class::cast)
        .filter(task -> matchesTaskId(task, domain.getAssignment().getId()))
        .findFirst()
        .orElse(null);
  }

  private boolean matchesTaskId(TaskJpaEntity task, UUID domainTaskId) {
    return task.getExternalReference() != null
        && task.getExternalReference().getPublicId() != null
        && task.getExternalReference().getPublicId().equals(domainTaskId);
  }

  private void mapAttempts(StudentTask domain, StudentTaskJpaEntity entity) {
    List<AttemptJpaEntity> attempts = domain.getAttempts().stream()
        .map(attempt -> {
          AttemptJpaEntity attemptEntity = attemptMapper.toEntity(attempt);
          attemptEntity.setStudentTask(entity);
          return attemptEntity;
        })
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

    entity.setAttempts(attempts);
  }
}