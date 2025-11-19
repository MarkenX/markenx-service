package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.classroom.domain.models.Student;
import com.udla.markenx.classroom.domain.models.StudentTask;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentAssignmentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.BaseMapper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.ExternalReferenceMapperHelper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.MapperValidator;

@Component
@RequiredArgsConstructor
public final class StudentMapper implements BaseMapper<Student, StudentJpaEntity> {

  private final StudentTaskMapper studentTaskMapper;
  private final MapperValidator validator;
  private final ExternalReferenceMapperHelper externalReferenceHelper;

  @Override
  public @NonNull Student toDomain(StudentJpaEntity entity) {
    validateEntity(entity);

    return new Student(
        extractPublicId(entity),
        extractCode(entity),
        entity.getId(),
        entity.getStatus(),
        extractCourseId(entity),
        entity.getFirstName(),
        entity.getLastName(),
        entity.getEmail(),
        mapTasksToDomain(entity),
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  /**
   * Lightweight mapping without loading tasks - optimized for list/simple DTOs
   */
  public @NonNull Student toDomainWithoutTasks(StudentJpaEntity entity) {
    validator.validateEntityNotNull(entity, StudentJpaEntity.class);
    validator.validateEntityField(entity.getExternalReference(), StudentJpaEntity.class, "externalReference");

    return new Student(
        extractPublicId(entity),
        extractCode(entity),
        entity.getId(),
        entity.getStatus(),
        extractCourseId(entity),
        entity.getFirstName(),
        entity.getLastName(),
        entity.getEmail(),
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  @Override
  public @NonNull StudentJpaEntity toEntity(Student domain) {
    return toEntity(domain, null);
  }

  public @NonNull StudentJpaEntity toEntity(Student domain, CourseJpaEntity parentCourse) {
    validateDomain(domain);

    StudentJpaEntity entity = new StudentJpaEntity();

    mapExternalReference(domain, entity);
    mapBasicFields(domain, entity);
    mapCourse(parentCourse, entity);
    mapTasks(domain, parentCourse, entity);

    return entity;
  }

  private void validateEntity(StudentJpaEntity entity) {
    validator.validateEntityNotNull(entity, StudentJpaEntity.class);
    validator.validateEntityField(entity.getExternalReference(), StudentJpaEntity.class, "externalReference");
  }

  private void validateDomain(Student domain) {
    validator.validateDomainNotNull(domain, Student.class);
    validator.validateDomainField(domain.getId(), Student.class, "id");
    validator.validateDomainStringField(domain.getFirstName(), Student.class, "firstName");
    validator.validateDomainStringField(domain.getLastName(), Student.class, "lastName");
    validator.validateDomainStringField(domain.getAcademicEmail(), Student.class, "academicEmail");
  }

  private UUID extractPublicId(StudentJpaEntity entity) {
    return externalReferenceHelper.extractPublicIdOrDefault(
        entity.getExternalReference(),
        UUID.randomUUID());
  }

  private String extractCode(StudentJpaEntity entity) {
    return externalReferenceHelper.extractCode(entity.getExternalReference());
  }

  private UUID extractCourseId(StudentJpaEntity entity) {
    if (entity.getCourse() != null && entity.getCourse().getExternalReference() != null) {
      return entity.getCourse().getExternalReference().getPublicId();
    }
    return null;
  }

  private List<StudentTask> mapTasksToDomain(StudentJpaEntity entity) {
    if (entity.getStudentAssignments() == null) {
      return List.of();
    }

    return entity.getStudentAssignments().stream()
        .filter(StudentTaskJpaEntity.class::isInstance)
        .map(StudentTaskJpaEntity.class::cast)
        .map(studentTaskMapper::toDomain)
        .toList();
  }

  private void mapExternalReference(Student domain, StudentJpaEntity entity) {
    // Use domain code if available, otherwise will be set after persistence
    entity.setExternalReference(
        externalReferenceHelper.createExternalReference(
            domain.getId(),
            domain.getCode(),
            "STUDENT"));
  }

  private void mapBasicFields(Student domain, StudentJpaEntity entity) {
    entity.setStatus(domain.getStatus());
    entity.setFirstName(domain.getFirstName());
    entity.setLastName(domain.getLastName());
    entity.setEmail(domain.getAcademicEmail());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());
    entity.setUpdatedBy(domain.getUpdatedBy());
  }

  private void mapCourse(CourseJpaEntity parentCourse, StudentJpaEntity entity) {
    if (parentCourse != null) {
      entity.setCourse(parentCourse);
    }
  }

  private void mapTasks(Student domain, CourseJpaEntity parentCourse, StudentJpaEntity entity) {
    if (domain.getAssignedTasks() == null) {
      entity.setStudentAssignments(List.of());
      return;
    }

    List<StudentAssignmentJpaEntity> tasks = domain.getAssignedTasks().stream()
        .map(task -> studentTaskMapper.toEntity(task, parentCourse))
        .map(StudentAssignmentJpaEntity.class::cast)
        .map(taskEntity -> {
          taskEntity.setStudent(entity);
          return taskEntity;
        })
        .toList();

    entity.setStudentAssignments(tasks);
  }
}