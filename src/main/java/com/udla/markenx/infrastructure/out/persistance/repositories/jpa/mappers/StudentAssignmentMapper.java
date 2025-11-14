package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.interfaces.StudentAssignment;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentAssignmentJpaEntity;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AssignmentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

import java.util.ArrayList;

public final class StudentAssignmentMapper {

  private StudentAssignmentMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static @NonNull StudentAssignment toDomain(StudentAssignmentJpaEntity entity) {
    if (entity == null) {
      throw new IllegalArgumentException("Entity is null");
    }
    Long assignmentId = null;
    Long studentId = null;
    if (entity.getAssignment() != null) {
      assignmentId = entity.getAssignment().getId();
    }
    if (entity.getStudent() != null) {
      studentId = entity.getStudent().getId();
    }

    StudentAssignment domain = new StudentAssignment(
        entity.getId(),
        assignmentId,
        studentId,
        entity.getActiveAttempt(),
        entity.getCurrentStatus(),
        new ArrayList<>());

    return domain;
  }

  public static @NonNull StudentAssignmentJpaEntity toEntity(StudentAssignment domain,
      AssignmentJpaEntity assignmentEntity,
      StudentJpaEntity studentEntity) {
    if (domain == null) {
      throw new IllegalArgumentException("Domain is null");
    }
    StudentAssignmentJpaEntity entity = new StudentAssignmentJpaEntity();
    entity.setId(domain.getId());
    entity.setAssignment(assignmentEntity);
    entity.setStudent(studentEntity);
    entity.setActiveAttempt(domain.getActiveAttempt());
    entity.setCurrentStatus(domain.getAssignmentStatus());
    return entity;
  }
}
