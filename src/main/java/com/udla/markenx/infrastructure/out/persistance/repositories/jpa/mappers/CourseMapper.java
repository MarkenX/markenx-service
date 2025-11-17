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
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.interfaces.AssignmentJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseMapper {

  private final StudentMapper studentMapper;
  private final AssignmentMapper assignmentMapper;
  // private final EntityManager entityManager;

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
      academicTermId = entity.getAcademicTerm().getExternalReference().getPublicId();
      academicTermYear = entity.getAcademicTerm().getAcademicYear();
    }

    Course domain = new Course(
        entity.getExternalReference().getPublicId(),
        entity.getExternalReference().getCode(),
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

  // entity.setId(domain.getSequence());

  public @NonNull CourseJpaEntity toEntity(Course domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    CourseJpaEntity entity = new CourseJpaEntity();
    entity.getExternalReference().setPublicId(domain.getId());
    entity.getExternalReference().setCode(domain.getCode());
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

    return entity;
  }
}

// if (domain.getAcademicTermId() != null) {
// AcademicTermJpaEntity ref =
// entityManager.getReference(AcademicTermJpaEntity.class,
// domain.getAcademicTermId());
// entity.setAcademicTerm(ref);
// }