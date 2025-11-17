package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Student;
import com.udla.markenx.core.models.StudentTask;
import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.interfaces.StudentAssignmentJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class StudentMapper {

  private final StudentTaskMapper studentTaskMapper;

  public @NonNull Student toDomain(StudentJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    UUID courseId = null;
    if (entity.getCourse() != null) {
      courseId = entity.getCourse().getExternalReference().getPublicId();
    }

    List<StudentTask> tasks = entity.getStudentAssignments().stream()
        .filter(StudentTaskJpaEntity.class::isInstance)
        .map(StudentTaskJpaEntity.class::cast)
        .map(studentTaskMapper::toDomain)
        .toList();

    Student domain = new Student(
        entity.getExternalReference().getPublicId(),
        entity.getExternalReference().getCode(),
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

    return domain;
  }

  public @NonNull StudentJpaEntity toEntity(Student domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    StudentJpaEntity entity = new StudentJpaEntity();
    entity.getExternalReference().setPublicId(domain.getId());
    entity.getExternalReference().setCode(domain.getCode());
    entity.setFirstName(domain.getFirstName());
    entity.setLastName(domain.getLastName());
    entity.setEmail(domain.getAcademicEmail());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());

    List<StudentAssignmentJpaEntity> tasks = domain.getAssignedTasks().stream()
        .map(studentTaskMapper::toEntity)
        .map(StudentAssignmentJpaEntity.class::cast)
        .peek(e -> e.setStudent(entity))
        .toList();
    entity.setStudentAssignments(tasks);

    return entity;
  }
}
