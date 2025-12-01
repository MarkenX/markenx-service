package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.ExternalReferenceJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class AcademicTermMapper {

  private final CourseMapper courseMapper;

  public AcademicTerm toDomain(AcademicTermJpaEntity entity, boolean includeCourses) {

    var domain = new AcademicTerm(
        extractId(entity),
        extractCode(entity),
        entity.getStatus(),
        entity.getStartOfTerm(),
        entity.getEndOfTerm(),
        entity.getAcademicYear(),
        entity.getTermNumber(),
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());

    if (includeCourses && entity.getCourses() != null && !entity.getCourses().isEmpty()) {
      List<Course> courses = entity.getCourses().stream().map(courseMapper::toDomain).toList();
      courses.forEach(domain::addCourse);
    }

    return domain;
  }

  public AcademicTermJpaEntity toEntity(AcademicTerm domain) {
    AcademicTermJpaEntity entity = new AcademicTermJpaEntity();

    ExternalReferenceJpaEntity ref = new ExternalReferenceJpaEntity();
    ref.setPublicId(domain.getId());
    ref.setCode(domain.getCode());
    ref.setEntityType(AcademicTerm.class.getSimpleName());

    entity.setExternalReference(ref);
    entity.setStatus(domain.getStatus());
    entity.setStartOfTerm(domain.getStartOfTerm());
    entity.setEndOfTerm(domain.getEndOfTerm());
    entity.setAcademicYear(domain.getAcademicYear());
    entity.setTermNumber(domain.getTermNumber());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());
    entity.setUpdatedBy(domain.getUpdatedBy());
    entity.setCourses(mapCourses(domain, entity));

    return entity;
  }

  private List<CourseJpaEntity> mapCourses(AcademicTerm domain, AcademicTermJpaEntity parent) {
    if (domain.getAssignedCourses() == null)
      return List.of();

    List<CourseJpaEntity> result = new ArrayList<>();

    for (Course course : domain.getAssignedCourses()) {
      if (course == null)
        continue;

      CourseJpaEntity c = courseMapper.toEntityWithoutParent(course);
      c.setAcademicTerm(parent);
      result.add(c);
    }

    return result;
  }

  private UUID extractId(AcademicTermJpaEntity entity) {
    return entity.getExternalReference() != null
        ? entity.getExternalReference().getPublicId()
        : UUID.randomUUID();
  }

  private String extractCode(AcademicTermJpaEntity entity) {
    return entity.getExternalReference() != null
        ? entity.getExternalReference().getCode()
        : null;
  }
}
