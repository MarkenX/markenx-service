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

public final class AcademicTermMapper {

  private AcademicTermMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static @NonNull AcademicTerm toDomain(AcademicTermJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    List<Course> courses = entity.getAssignedCourses() != null
        ? entity.getAssignedCourses().stream()
            .map(CourseMapper::toDomain)
            .toList()
        : List.of();

    AcademicTerm domain = new AcademicTerm(
        entity.getPublicId(),
        entity.getCode(),
        entity.getStatus(),
        entity.getStartOfTerm(),
        entity.getEndOfTerm(),
        entity.getAcademicYear(),
        courses,
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());

    return domain;
  }

  public static @NonNull AcademicTermJpaEntity toEntity(AcademicTerm domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    AcademicTermJpaEntity entity = new AcademicTermJpaEntity();

    entity.setPublicId(domain.getId());
    entity.setCode(domain.getCode());
    entity.setStatus(domain.getStatus());
    entity.setStartOfTerm(domain.getStartOfTerm());
    entity.setEndOfTerm(domain.getEndOfTerm());
    entity.setAcademicYear(domain.getAcademicYear());
    entity.setTermNumber(domain.getTermNumber());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());

    if (domain.getAssignedCourses() != null) {
      List<CourseJpaEntity> courses = domain.getAssignedCourses().stream()
          .map(CourseMapper::toEntity)
          .peek(e -> e.setAcademicTerm(entity))
          .toList();
      entity.setAssignedCourses(courses);
    }

    return entity;
  }
}
