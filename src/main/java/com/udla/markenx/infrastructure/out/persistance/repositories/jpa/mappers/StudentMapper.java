package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Student;
import com.udla.markenx.core.models.StudentTask;

import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class StudentMapper {

  private final StudentTaskMapper studentTaskMapper;
  private final EntityManager entityManager;

  public @NonNull Student toDomain(StudentJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    UUID courseId = null;
    if (entity.getCourse() != null) {
      courseId = entity.getCourse().getPublicId();
    }

    List<StudentTask> tasks = entity.getTasks().stream()
        .map(studentTaskMapper::toDomain)
        .toList();

    Student domain = new Student(
        entity.getPublicId(),
        entity.getCode(),
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
    entity.setPublicId(domain.getId());
    entity.setCode(domain.getCode());
    entity.setId(domain.getSequence());
    entity.setFirstName(domain.getFirstName());
    entity.setLastName(domain.getLastName());
    entity.setEmail(domain.getEmail());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());

    List<StudentTaskJpaEntity> tasks = domain.getAssignedTaks().stream()
        .map(studentTaskMapper::toEntity)
        .peek(e -> e.setStudent(entity))
        .toList();
    entity.setTasks(tasks);

    if (domain.getCourseId() != null) {
      CourseJpaEntity ref = entityManager.getReference(CourseJpaEntity.class, domain.getCourseId());
      entity.setCourse(ref);
    }

    return entity;
  }
}
