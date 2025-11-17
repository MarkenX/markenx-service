package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.AcademicTerm;
import com.udla.markenx.core.models.Course;
import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.ExternalReferenceJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class AcademicTermMapper {

  private final CourseMapper courseMapper;

  public @NonNull AcademicTerm toDomain(AcademicTermJpaEntity entity) {
    if (entity == null) {
      // throw new DomainMappingException("AcademicTermJpaEntity cannot be null.");
    }

    List<CourseJpaEntity> courseEntities = entity.getCourses() != null ? entity.getCourses() : List.of();

    List<Course> courses = courseEntities.stream()
        .map(courseMapper::toDomain)
        .toList();

    return new AcademicTerm(
        entity.getExternalReference() != null ? entity.getExternalReference().getPublicId()
            : java.util.UUID.randomUUID(),
        entity.getExternalReference() != null ? entity.getExternalReference().getCode() : "",
        entity.getStatus(),
        entity.getStartOfTerm(),
        entity.getEndOfTerm(),
        entity.getAcademicYear(),
        courses,
        entity.getCreatedBy(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }

  public @NonNull AcademicTermJpaEntity toEntity(AcademicTerm domain) {
    if (domain == null) {
      // throw new EntityMappingException("AcademicTerm cannot be null.");
    }

    AcademicTermJpaEntity entity = new AcademicTermJpaEntity();

    // ----- External Reference -----
    ExternalReferenceJpaEntity ref = new ExternalReferenceJpaEntity();
    ref.setPublicId(domain.getId());
    ref.setCode(domain.getCode());
    ref.setEntityType("ACADEMIC_TERM");
    entity.setExternalReference(ref);

    // ----- Simple Fields -----
    entity.setStatus(domain.getStatus());
    entity.setStartOfTerm(domain.getStartOfTerm());
    entity.setEndOfTerm(domain.getEndOfTerm());
    entity.setAcademicYear(domain.getAcademicYear());
    entity.setTermNumber(domain.getTermNumber());
    entity.setCreatedBy(domain.getCreatedBy());
    entity.setCreatedAt(domain.getCreatedAtDateTime());
    entity.setUpdatedAt(domain.getUpdatedAtDateTime());
    entity.setUpdatedBy(domain.getUpdatedBy());

    // ----- Courses (children) -----
    List<CourseJpaEntity> courses = entity.getCourses();
    if (courses == null) {
      courses = new ArrayList<>();
      entity.setCourses(courses);
    } else {
      courses.clear();
    }

    if (domain.getAssignedCourses() != null) {
      for (Course courseDomain : domain.getAssignedCourses()) {

        if (courseDomain == null)
          continue; // defensivo

        CourseJpaEntity courseEntity = courseMapper.toEntity(courseDomain);

        // maintain bidirectional relationship
        courseEntity.setAcademicTerm(entity);

        courses.add(courseEntity);
      }
    }

    return entity;
  }
}
