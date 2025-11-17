package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.AcademicTerm;
import com.udla.markenx.core.models.Course;
import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class AcademicTermMapper {

  private final CourseMapper courseMapper;

  public @NonNull AcademicTerm toDomain(AcademicTermJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    List<Course> courses = entity.getCourses() != null
        ? entity.getCourses().stream()
            .map(courseMapper::toDomain)
            .toList()
        : List.of();

    AcademicTerm domain = new AcademicTerm(
        entity.getExternalReference().getPublicId(),
        entity.getExternalReference().getCode(),
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

  public @NonNull AcademicTermJpaEntity toEntity(AcademicTerm domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    AcademicTermJpaEntity entity = new AcademicTermJpaEntity();

    entity.getExternalReference().setPublicId(domain.getId());
    entity.getExternalReference().setCode(domain.getCode());
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
          .map(courseMapper::toEntity)
          .peek(e -> e.setAcademicTerm(entity))
          .toList();
      entity.setCourses(courses);
    }

    return entity;
  }
}
