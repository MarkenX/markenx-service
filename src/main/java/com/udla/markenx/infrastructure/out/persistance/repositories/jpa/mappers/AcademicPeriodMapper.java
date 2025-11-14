package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.models.AcademicTerm;
import com.udla.markenx.core.models.Course;

import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;

public final class AcademicPeriodMapper {

  private AcademicPeriodMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static @NonNull AcademicTerm toDomain(AcademicTermJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    List<Course> courses = entity.getCourses() != null
        ? entity.getCourses().stream()
            .map(CourseMapper::toDomain)
            .toList()
        : List.of();

    AcademicTerm domain = new AcademicTerm(
        entity.getId(),
        entity.getStartDate(),
        entity.getEndDate(),
        entity.getYear(),
        entity.getTermNumber(),
        courses);

    return domain;
  }

  public static @NonNull AcademicTermJpaEntity toEntity(AcademicTerm domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    AcademicTermJpaEntity entity = new AcademicTermJpaEntity();
    entity.setId(domain.getId());
    entity.setStartDate(domain.getStartOfTerm());
    entity.setEndDate(domain.getEndOfTerm());
    entity.setYear(domain.getAcademicYear());
    entity.setTermNumber(domain.getTermNumber());

    if (domain.getAssignedCourses() != null) {
      List<CourseJpaEntity> courses = domain.getAssignedCourses().stream()
          .map(CourseMapper::toEntity)
          .peek(e -> e.setAcademicPeriod(entity))
          .toList();
      entity.setCourses(courses);
    }

    return entity;
  }
}
