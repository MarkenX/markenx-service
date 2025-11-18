package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.classroom.domain.interfaces.Assignment;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.domain.models.Student;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AssignmentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.AcademicTermJpaRepository;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.BaseMapper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.ExternalReferenceMapperHelper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.MapperValidator;

@Component
@RequiredArgsConstructor
public class CourseMapper implements BaseMapper<Course, CourseJpaEntity> {

  private final StudentMapper studentMapper;
  private final AssignmentMapper assignmentMapper;
  private final MapperValidator validator;
  private final ExternalReferenceMapperHelper externalReferenceHelper;
  private final AcademicTermJpaRepository academicTermRepository;

  /**
   * Converts domain to entity with full relationship resolution.
   * Used for standalone Course creation/update operations.
   */
  public @NonNull CourseJpaEntity toEntity(Course domain, CourseJpaEntity entity) {
    validateDomain(domain);

    final CourseJpaEntity target = entity != null ? entity : new CourseJpaEntity();

    mapExternalReference(domain, target);
    mapBasicFields(domain, target);
    mapAcademicTerm(domain, target);
    mapAssignments(domain, target);
    mapStudents(domain, target);

    return target;
  }

  /**
   * Converts domain to entity WITHOUT resolving AcademicTerm from database.
   * Used when mapping as part of a parent aggregate (e.g., AcademicTermMapper).
   * The parent mapper will set the AcademicTerm reference after this call.
   */
  public @NonNull CourseJpaEntity toEntityWithoutParent(Course domain) {
    validateDomain(domain);

    final CourseJpaEntity target = new CourseJpaEntity();

    mapExternalReference(domain, target);
    mapBasicFields(domain, target);
    // Skip mapAcademicTerm - will be set by parent mapper
    mapAssignments(domain, target);
    mapStudents(domain, target);

    return target;
  }

  @Override
  public @NonNull CourseJpaEntity toEntity(Course domain) {
    return toEntity(domain, null);
  }

  @Override
  public @NonNull Course toDomain(CourseJpaEntity entity) {
    validateEntity(entity);

    return new Course(
        extractPublicId(entity),
        extractCode(entity),
        entity.getId(),
        entity.getStatus(),
        extractAcademicTermId(entity),
        extractAcademicTermYear(entity),
        entity.getName(),
        mapStudentsToDomain(entity),
        mapAssignmentsToDomain(entity),
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  /**
   * Lightweight mapping without loading students/assignments - optimized for
   * list/simple DTOs
   */
  public @NonNull Course toDomainWithoutRelations(CourseJpaEntity entity) {
    validator.validateEntityNotNull(entity, CourseJpaEntity.class);
    validator.validateEntityField(entity.getExternalReference(), CourseJpaEntity.class, "externalReference");

    return new Course(
        extractPublicId(entity),
        extractCode(entity),
        entity.getId(),
        entity.getStatus(),
        extractAcademicTermId(entity),
        extractAcademicTermYear(entity),
        entity.getName(),
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  private void validateDomain(Course domain) {
    validator.validateDomainNotNull(domain, Course.class);
    validator.validateDomainField(domain.getId(), Course.class, "id");
    validator.validateDomainStringField(domain.getName(), Course.class, "name");
    validator.validateDomainField(domain.getAssignments(), Course.class, "assignments");
    validator.validateDomainField(domain.getStudents(), Course.class, "students");
  }

  private void validateEntity(CourseJpaEntity entity) {
    validator.validateEntityNotNull(entity, CourseJpaEntity.class);
    validator.validateEntityField(entity.getExternalReference(), CourseJpaEntity.class, "externalReference");
    validator.validateEntityField(entity.getStudents(), CourseJpaEntity.class, "students");
    validator.validateEntityField(entity.getAssignments(), CourseJpaEntity.class, "assignments");
  }

  private void mapExternalReference(Course domain, CourseJpaEntity target) {
    target.setExternalReference(
        externalReferenceHelper.createExternalReference(
            domain.getId(),
            domain.getCode(),
            "COURSE"));
  }

  private void mapBasicFields(Course domain, CourseJpaEntity target) {
    target.setId(domain.getSequence());
    target.setStatus(domain.getStatus());
    target.setName(domain.getName());
    target.setCreatedBy(domain.getCreatedBy());
    target.setCreatedAt(domain.getCreatedAtDateTime());
    target.setUpdatedAt(domain.getUpdatedAtDateTime());
    target.setUpdatedBy(domain.getUpdatedBy());
  }

  private void mapAcademicTerm(Course domain, CourseJpaEntity target) {
    if (domain.getAcademicTermId() == null) {
      target.setAcademicTerm(null);
      return;
    }

    // Find the existing AcademicTermJpaEntity by UUID
    AcademicTermJpaEntity academicTerm = academicTermRepository.findAll().stream()
        .filter(term -> term.getExternalReference() != null &&
            term.getExternalReference().getPublicId().equals(domain.getAcademicTermId()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(
            "AcademicTerm not found with UUID: " + domain.getAcademicTermId()));

    target.setAcademicTerm(academicTerm);
  }

  private void mapAssignments(Course domain, CourseJpaEntity target) {
    target.getAssignments().clear();

    List<AssignmentJpaEntity> assignments = domain.getAssignments().stream()
        .map(assignment -> {
          AssignmentJpaEntity entity = assignmentMapper.toEntity(assignment, null);
          entity.setCourse(target);
          return entity;
        })
        .toList();

    target.getAssignments().addAll(assignments);
  }

  private void mapStudents(Course domain, CourseJpaEntity target) {
    target.getStudents().clear();

    List<StudentJpaEntity> students = domain.getStudents().stream()
        .map(student -> {
          StudentJpaEntity entity = studentMapper.toEntity(student, target);
          entity.setCourse(target);
          return entity;
        })
        .toList();

    target.getStudents().addAll(students);
  }

  private UUID extractPublicId(CourseJpaEntity entity) {
    return externalReferenceHelper.extractPublicId(
        entity.getExternalReference(),
        CourseJpaEntity.class);
  }

  private String extractCode(CourseJpaEntity entity) {
    return externalReferenceHelper.extractCode(entity.getExternalReference());
  }

  private UUID extractAcademicTermId(CourseJpaEntity entity) {
    if (entity.getAcademicTerm() != null &&
        entity.getAcademicTerm().getExternalReference() != null) {
      return entity.getAcademicTerm().getExternalReference().getPublicId();
    }
    return null;
  }

  private int extractAcademicTermYear(CourseJpaEntity entity) {
    if (entity.getAcademicTerm() != null) {
      return entity.getAcademicTerm().getAcademicYear();
    }
    return -1;
  }

  private List<Student> mapStudentsToDomain(CourseJpaEntity entity) {
    return entity.getStudents().stream()
        .map(studentMapper::toDomain)
        .toList();
  }

  private List<Assignment> mapAssignmentsToDomain(CourseJpaEntity entity) {
    return entity.getAssignments().stream()
        .map(assignmentMapper::toDomain)
        .toList();
  }
}