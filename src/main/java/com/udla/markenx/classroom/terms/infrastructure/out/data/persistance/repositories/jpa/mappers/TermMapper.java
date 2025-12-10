package com.udla.markenx.classroom.terms.infrastructure.out.data.persistance.repositories.jpa.mappers;

import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.CourseMapper;
import com.udla.markenx.classroom.terms.domain.model.Term;
import com.udla.markenx.classroom.terms.domain.model.TermFactory;
import com.udla.markenx.classroom.terms.infrastructure.out.data.persistance.repositories.jpa.entities.TermJpaEntity;
import com.udla.markenx.shared.domain.valueobjects.AuditParams;
import com.udla.markenx.shared.domain.valueobjects.DateInterval;
import com.udla.markenx.shared.domain.valueobjects.EntityInfo;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.ExternalReferenceJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public final class TermMapper {

    private final CourseMapper courseMapper;

    public Term toDomain(TermJpaEntity entity) {
        var entityInfo = new EntityInfo(entity.getExternalReference().getPublicId(), entity.getStatus());
        var dateInterval = new DateInterval(entity.getStartDate(), entity.getEndDate());
        var auditParams = new AuditParams(entity.getCreatedBy(), entity.getUpdatedBy(), entity.getCreatedAt(), entity.getUpdatedAt());
        return TermFactory.restoreSingleYearFromRepository(
                entityInfo,
                dateInterval,
                entity.getId(),
                auditParams);
    }

    public TermJpaEntity toEntity(Term domain) {
        TermJpaEntity entity = new TermJpaEntity();

        ExternalReferenceJpaEntity ref = new ExternalReferenceJpaEntity();
        ref.setPublicId(domain.getId());
        ref.setCode(domain.getCode());
        ref.setEntityType(Term.class.getSimpleName());

        entity.setExternalReference(ref);
        entity.setStatus(domain.getStatus());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setYear(domain.getYear());
        entity.setSequence(domain.getSequence());
        entity.setCreatedBy(domain.getCreatedBy());
        entity.setCreatedAt(domain.getCreatedAtDateTime());
        entity.setUpdatedAt(domain.getUpdatedAtDateTime());
        entity.setUpdatedBy(domain.getUpdatedBy());
        entity.setCourses(mapCourses(domain, entity));

        return entity;
    }

    private List<CourseJpaEntity> mapCourses(Term domain, TermJpaEntity parent) {
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

    private UUID extractId(TermJpaEntity entity) {
        return entity.getExternalReference() != null
                ? entity.getExternalReference().getPublicId()
                : UUID.randomUUID();
    }

    private String extractCode(TermJpaEntity entity) {
        return entity.getExternalReference() != null
                ? entity.getExternalReference().getCode()
                : null;
    }
}
