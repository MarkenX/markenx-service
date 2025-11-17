package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.domain.models.Student;
import com.udla.markenx.classroom.domain.models.StudentTask;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.ExternalReferenceJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentAssignmentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;

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
    if (entity.getCourse() != null && entity.getCourse().getExternalReference() != null) {
      courseId = entity.getCourse().getExternalReference().getPublicId();
    }

    List<StudentTask> tasks = entity.getStudentAssignments().stream()
        .filter(StudentTaskJpaEntity.class::isInstance)
        .map(StudentTaskJpaEntity.class::cast)
        .map(studentTaskMapper::toDomain)
        .toList();

    Student domain = new Student(
        entity.getExternalReference() != null ? entity.getExternalReference().getPublicId()
            : java.util.UUID.randomUUID(),
        entity.getExternalReference() != null ? entity.getExternalReference().getCode() : "",
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

  public @NonNull StudentJpaEntity toEntity(Student domain, CourseJpaEntity parentCourse) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    StudentJpaEntity entity = new StudentJpaEntity();

    ExternalReferenceJpaEntity ref = new ExternalReferenceJpaEntity();
    ref.setPublicId(domain.getId());
    ref.setCode(domain.getCode());
    ref.setEntityType("STUDENT");

    entity.setExternalReference(ref);
    entity.setStatus(domain.getStatus());
    entity.setFirstName(domain.getFirstName());
    entity.setLastName(domain.getLastName());
    entity.setEmail(domain.getAcademicEmail());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());
    entity.setUpdatedBy(domain.getUpdatedBy());

    if (parentCourse != null) {
      entity.setCourse(parentCourse);
    }

    List<StudentAssignmentJpaEntity> tasks = domain.getAssignedTasks().stream()
        .map(a -> studentTaskMapper.toEntity(a, parentCourse))
        .map(StudentAssignmentJpaEntity.class::cast)
        .peek(e -> e.setStudent(entity))
        .toList();
    entity.setStudentAssignments(tasks);

    return entity;
  }
}
