package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.interfaces.Assignment;

import com.udla.markenx.core.models.Course;
import com.udla.markenx.core.models.Student;

import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AssignmentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

public class CourseMapper {

  private CourseMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static @NonNull Course toDomain(CourseJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    List<Assignment> assignments = entity.getAssignments().stream()
        .map(AssignmentMapper::toDomain)
        .toList();

    List<Student> students = entity.getStudents().stream()
        .map(StudentMapper::toDomain)
        .toList();

    Course domain = new Course(entity.getId(), assignments, students);

    return domain;
  }

  public static @NonNull CourseJpaEntity toEntity(Course domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    CourseJpaEntity entity = new CourseJpaEntity();
    entity.setId(domain.getId());

    List<AssignmentJpaEntity> assignments = domain.getAssignments().stream()
        .map(AssignmentMapper::toEntity)
        .peek(e -> e.setCourse(entity))
        .toList();
    entity.setAssignments(assignments);

    List<StudentJpaEntity> students = domain.getStudents().stream()
        .map(StudentMapper::toEntity)
        .peek(e -> e.setCourse(entity))
        .toList();
    entity.setStudents(students);

    return entity;
  }
}
