package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.BaseMapper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.ExternalReferenceMapperHelper;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.MapperValidator;

@Component
@RequiredArgsConstructor
public final class TaskMapper implements BaseMapper<Task, TaskJpaEntity> {

	private final MapperValidator validator;
	private final ExternalReferenceMapperHelper externalReferenceHelper;

	@Override
	public @NonNull Task toDomain(TaskJpaEntity entity) {
		validateEntity(entity);

		return new Task(
				extractPublicId(entity),
				extractCode(entity),
				entity.getId(),
				entity.getStatus(),
				extractCourseId(entity),
				extractAcademicTermYear(entity),
				entity.getTitle(),
				entity.getSummary(),
				entity.getDueDate(),
				entity.getMaxAttempts(),
				entity.getMinScoreToPass(),
				entity.getCreatedBy(),
				entity.getCreatedAt(),
				entity.getUpdatedAt());
	}

	@Override
	public @NonNull TaskJpaEntity toEntity(Task domain) {
		return toEntity(domain, null);
	}

	public @NonNull TaskJpaEntity toEntity(Task domain, CourseJpaEntity parentCourse) {
		validateDomain(domain);

		TaskJpaEntity entity = new TaskJpaEntity();

		mapExternalReference(domain, entity);
		mapBasicFields(domain, entity);
		mapCourse(parentCourse, entity);

		return entity;
	}

	private void validateEntity(TaskJpaEntity entity) {
		validator.validateEntityNotNull(entity, TaskJpaEntity.class);
		validator.validateEntityField(entity.getExternalReference(), TaskJpaEntity.class, "externalReference");
	}

	private void validateDomain(Task domain) {
		validator.validateDomainNotNull(domain, Task.class);
		validator.validateDomainField(domain.getId(), Task.class, "id");
		validator.validateDomainStringField(domain.getTitle(), Task.class, "title");
	}

	private UUID extractPublicId(TaskJpaEntity entity) {
		return externalReferenceHelper.extractPublicId(
				entity.getExternalReference(),
				TaskJpaEntity.class);
	}

	private String extractCode(TaskJpaEntity entity) {
		return externalReferenceHelper.extractCode(entity.getExternalReference());
	}

	private UUID extractCourseId(TaskJpaEntity entity) {
		if (entity.getCourse() != null && entity.getCourse().getExternalReference() != null) {
			return entity.getCourse().getExternalReference().getPublicId();
		}
		return null;
	}

	private int extractAcademicTermYear(TaskJpaEntity entity) {
		if (entity.getCourse() != null && entity.getCourse().getAcademicTerm() != null) {
			return entity.getCourse().getAcademicTerm().getAcademicYear();
		}
		return -1;
	}

	private void mapExternalReference(Task domain, TaskJpaEntity entity) {
		entity.setExternalReference(
				externalReferenceHelper.createExternalReference(
						domain.getId(),
						domain.getCode(),
						"TASK"));
	}

	private void mapBasicFields(Task domain, TaskJpaEntity entity) {
		entity.setStatus(domain.getEntityStatus());
		entity.setTitle(domain.getTitle());
		entity.setSummary(domain.getSummary());
		entity.setDueDate(domain.getDueDate());
		entity.setMaxAttempts(domain.getMaxAttempts());
		entity.setMinScoreToPass(domain.getMinScoreToPass());
		entity.setCreatedBy(domain.getCreatedBy());
		entity.setCreatedAt(domain.getCreatedAtDateTime());
		entity.setUpdatedAt(domain.getUpdatedAtDateTime());
		entity.setUpdatedBy(domain.getUpdatedBy());
	}

	private void mapCourse(CourseJpaEntity parentCourse, TaskJpaEntity entity) {
		entity.setCourse(parentCourse);
	}
}