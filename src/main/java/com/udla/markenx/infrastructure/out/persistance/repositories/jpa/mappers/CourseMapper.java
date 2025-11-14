package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.core.interfaces.Assignment;

import com.udla.markenx.core.models.Course;
import com.udla.markenx.core.models.Student;

import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AssignmentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseMapper {

  private final StudentMapper studentMapper;
  private final AssignmentMapper assignmentMapper;
  private final EntityManager entityManager;

  public @NonNull Course toDomain(CourseJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    List<Assignment> assignments = entity.getAssignments().stream()
        .map(assignmentMapper::toDomain)
        .toList();

    List<Student> students = entity.getStudents().stream()
        .map(studentMapper::toDomain)
        .toList();

    UUID academicTermId = null;
    int academicTermYear = -1;
    if (entity.getAcademicTerm() != null) {
      academicTermId = entity.getAcademicTerm().getPublicId();
      academicTermYear = entity.getAcademicTerm().getAcademicYear();
    }

    Course domain = new Course(
        entity.getPublicId(),
        entity.getCode(),
        entity.getId(),
        entity.getStatus(),
        academicTermId,
        academicTermYear,
        entity.getName(),
        students,
        assignments,
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());

    return domain;
  }

  public @NonNull CourseJpaEntity toEntity(Course domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    CourseJpaEntity entity = new CourseJpaEntity();
    entity.setPublicId(domain.getId());
    entity.setCode(domain.getCode());
    entity.setId(domain.getSequence());
    entity.setStatus(domain.getStatus());
    entity.setName(domain.getName());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());

    List<StudentJpaEntity> students = domain.getStudents().stream()
        .map(student -> studentMapper.toEntity(student))
        .peek(e -> e.setCourse(entity))
        .toList();
    entity.setStudents(students);

    List<AssignmentJpaEntity> assignments = domain.getAssignments().stream()
        .map(assignmentMapper::toEntity)
        .peek(e -> e.setCourse(entity))
        .toList();
    entity.setAssignments(assignments);

    if (domain.getAcademicTermId() != null) {
      AcademicTermJpaEntity ref = entityManager.getReference(AcademicTermJpaEntity.class, domain.getAcademicTermId());
      entity.setAcademicTerm(ref);
    }

    return entity;
  }
}
