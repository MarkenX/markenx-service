package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.interfaces.Assignment;

import com.udla.markenx.core.models.Course;
import com.udla.markenx.core.valueobjects.AuditInfo;
import com.udla.markenx.core.models.Student;

import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AssignmentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

/**
 * Mapper for converting between Course domain models and CourseJpaEntity.
 * 
 * Handles conversion of Course objects and their nested collections
 * (students, tasks, assignments).
 * 
 * Utility class with static methods only - cannot be instantiated.
 */
public class CourseMapper {

  /**
   * Private constructor to prevent instantiation.
   * This is a utility class with only static methods.
   */
  private CourseMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  /**
   * Converts a CourseJpaEntity to a Course domain model.
   * Recursively converts all nested assignments and students.
   * 
   * @param entity the CourseJpaEntity from database
   * @return the Course domain model
   * @throws DomainMappingException if entity is null
   */
  public static @NonNull Course toDomain(CourseJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    // Convert all assignments from JPA entities to domain models
    List<Assignment> assignments = entity.getAssignments().stream()
        .map(AssignmentMapper::toDomain)
        .toList();

    // Convert all students from JPA entities to domain models
    List<Student> students = entity.getStudents().stream()
        .map(StudentMapper::toDomain)
        .toList();

    // Create AuditInfo from entity audit fields
    AuditInfo timestamps = new AuditInfo(entity.getCreatedBy(), entity.getCreatedAt(), entity.getLastModifiedAt());

    // Extract academic period id if present
    Long academicPeriodId = null;
    if (entity.getAcademicPeriod() != null) {
      academicPeriodId = entity.getAcademicPeriod().getId();
    }

    // Create domain model with all converted data and audit info
    Course domain = new Course(entity.getId(), assignments, students, academicPeriodId, entity.getLabel(), timestamps);

    return domain;
  }

  /**
   * Converts a Course domain model to a JPA entity.
   * Recursively converts all assignments and students.
   * 
   * @param domain the Course domain model
   * @return the CourseJpaEntity
   * @throws EntityMappingException if domain is null
   */
  public static @NonNull CourseJpaEntity toEntity(Course domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    CourseJpaEntity entity = new CourseJpaEntity();
    entity.setId(domain.getId());

    // Convert all students and set bidirectional relationship first so
    // assignments mapping can refer to the persisted student entities.
    List<StudentJpaEntity> students = domain.getStudents().stream()
        .map(student -> StudentMapper.toEntity(student, null))
        .peek(e -> e.setCourse(entity))
        .toList();
    entity.setStudents(students);

    // Convert all assignments and set bidirectional relationship
    List<AssignmentJpaEntity> assignments = domain.getAssignments().stream()
        .map(AssignmentMapper::toEntity)
        .peek(e -> e.setCourse(entity))
        .toList();
    entity.setAssignments(assignments);

    // For tasks, ensure each attempt's student reference is the corresponding
    // StudentJpaEntity instance mapped above (match by first+last name or email)
    if (entity.getAssignments() != null && !entity.getAssignments().isEmpty() && entity.getStudents() != null) {
      // We rely on the stream ordering to be identical between domain assignments
      // and the produced entity assignments. Iterate by index and, for each task
      // assignment, match each domain attempt's student to the corresponding
      // StudentJpaEntity previously created above.
      for (int i = 0; i < domain.getAssignments().size(); i++) {
        Assignment domainAssignment = domain.getAssignments().get(i);
        AssignmentJpaEntity entityAssignment = entity.getAssignments().get(i);
        if (domainAssignment instanceof com.udla.markenx.core.models.Task
            && entityAssignment instanceof com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity) {
          com.udla.markenx.core.models.Task domainTask = (com.udla.markenx.core.models.Task) domainAssignment;
          com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity taskEntity = (com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity) entityAssignment;

          // For each attempt in the domain task, find the corresponding AttemptJpaEntity
          // by index
          for (int j = 0; j < domainTask.getAttempts().size() && j < taskEntity.getAttempts().size(); j++) {
            com.udla.markenx.core.models.Attempt domainAttempt = domainTask.getAttempts().get(j);
            com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity attemptEntity = taskEntity
                .getAttempts().get(j);

            Long domainStudentId = domainAttempt.getStudent();
            if (domainStudentId != null) {
              com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity match = null;
              for (com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity s : entity
                  .getStudents()) {
                if (s.getId() != null && s.getId().equals(domainStudentId)) {
                  match = s;
                  break;
                }
              }
              if (match != null) {
                attemptEntity.setStudent(match);
              }
            }
          }
        }
      }
    }

    // Map label
    entity.setLabel(domain.getName());

    // Map academic period id if present on domain
    if (domain.getAcademicTermId() != null) {
      com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicPeriodJpaEntity ap = new com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicPeriodJpaEntity();
      ap.setId(domain.getAcademicTermId());
      entity.setAcademicPeriod(ap);
    }

    return entity;
  }
}
