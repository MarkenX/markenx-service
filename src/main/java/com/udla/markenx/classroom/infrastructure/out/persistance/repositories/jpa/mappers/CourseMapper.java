package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.domain.interfaces.Assignment;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.domain.models.Student;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AssignmentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.ExternalReferenceJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CourseMapper {

  private final StudentMapper studentMapper;
  private final AssignmentMapper assignmentMapper;

  public @NonNull CourseJpaEntity toEntity(Course domain, CourseJpaEntity entity) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    // must be effectively final
    final CourseJpaEntity target = (entity != null) ? entity : new CourseJpaEntity();

    // External reference
    ExternalReferenceJpaEntity ref = (target.getExternalReference() != null)
        ? target.getExternalReference()
        : new ExternalReferenceJpaEntity();

    ref.setPublicId(domain.getId());
    ref.setCode(domain.getCode());
    ref.setEntityType("COURSE");
    target.setExternalReference(ref);

    target.setId(domain.getSequence());
    target.setStatus(domain.getStatus());
    target.setName(domain.getName());
    target.setCreatedBy(domain.getCreatedBy());
    target.setCreatedAt(domain.getCreatedAtDateTime());
    target.setUpdatedAt(domain.getUpdatedAtDateTime());
    target.setUpdatedBy(domain.getUpdatedBy());

    // Academic Term
    if (domain.getAcademicTermId() != null) {
      AcademicTermJpaEntity termRef = new AcademicTermJpaEntity();
      ExternalReferenceJpaEntity termExt = new ExternalReferenceJpaEntity();
      termExt.setPublicId(domain.getAcademicTermId());
      termExt.setEntityType("ACADEMIC_TERM");
      termRef.setExternalReference(termExt);
      target.setAcademicTerm(termRef);
    } else {
      target.setAcademicTerm(null);
    }

    // Assignments
    target.getAssignments().clear();
    List<AssignmentJpaEntity> assignments = domain.getAssignments().stream()
        .map(a -> assignmentMapper.toEntity(a, null))
        .peek(ae -> ae.setCourse(target))
        .toList();
    target.getAssignments().addAll(assignments);

    // Students (after assignments so StudentTask can link existing tasks)
    target.getStudents().clear();
    List<StudentJpaEntity> students = domain.getStudents().stream()
        .map(s -> studentMapper.toEntity(s, target))
        .peek(se -> se.setCourse(target))
        .toList();
    target.getStudents().addAll(students);

    return target;
  }

  public @NonNull CourseJpaEntity toEntity(Course domain) {
    return toEntity(domain, null);
  }

  public @NonNull Course toDomain(CourseJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    UUID academicTermId = null;
    int academicTermYear = -1;
    if (entity.getAcademicTerm() != null && entity.getAcademicTerm().getExternalReference() != null) {
      academicTermId = entity.getAcademicTerm().getExternalReference().getPublicId();
      academicTermYear = entity.getAcademicTerm().getAcademicYear();
    }

    List<Student> students = entity.getStudents() != null
        ? entity.getStudents().stream().map(studentMapper::toDomain).toList()
        : List.of();

    List<Assignment> assignments = entity.getAssignments() != null
        ? entity.getAssignments().stream().map(assignmentMapper::toDomain).toList()
        : List.of();

    return new Course(
        entity.getExternalReference() != null ? entity.getExternalReference().getPublicId() : UUID.randomUUID(),
        entity.getExternalReference() != null ? entity.getExternalReference().getCode() : "",
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
  }
}
