package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.core.models.Task;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.ExternalReferenceJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class TaskMapper {

	public @NonNull Task toDomain(TaskJpaEntity entity) {
		if (entity == null) {
			throw new DomainMappingException();
		}

		UUID courseId = null;
		int academicTermYear = -1;
		if (entity.getCourse() != null) {
			if (entity.getCourse().getExternalReference() != null) {
				courseId = entity.getCourse().getExternalReference().getPublicId();
			}
			if (entity.getCourse().getAcademicTerm() != null) {
				academicTermYear = entity.getCourse().getAcademicTerm().getAcademicYear();
			}
		}

		Task domain = new Task(
				entity.getExternalReference().getPublicId(),
				entity.getExternalReference().getCode(),
				entity.getId(),
				entity.getStatus(),
				courseId,
				academicTermYear,
				entity.getTitle(),
				entity.getSummary(),
				entity.getDueDate(),
				entity.getMaxAttempts(),
				entity.getMinScoreToPass(),
				entity.getCreatedBy(),
				entity.getCreatedAt(),
				entity.getUpdatedAt());

		return domain;
	}

	public @NonNull TaskJpaEntity toEntity(Task domain, CourseJpaEntity parentCourse) {
		if (domain == null) {
			throw new EntityMappingException();
		}

		TaskJpaEntity entity = new TaskJpaEntity();

		ExternalReferenceJpaEntity ref = new ExternalReferenceJpaEntity();
		ref.setPublicId(domain.getId());
		ref.setCode(domain.getCode());
		ref.setEntityType("TASK");

		entity.setExternalReference(ref);
		entity.setStatus(domain.getStatus());
		entity.setTitle(domain.getTitle());
		entity.setSummary(domain.getSummary());
		entity.setDueDate(domain.getDueDate());
		entity.setMaxAttempts(domain.getMaxAttempts());
		entity.setMinScoreToPass(domain.getMinScoreToPass());
		entity.setCreatedBy(domain.getCreatedBy());
		entity.setCreatedAt(domain.getCreatedAtDateTime());
		entity.setUpdatedAt(domain.getUpdatedAtDateTime());
		entity.setUpdatedBy(domain.getUpdatedBy());
		entity.setCourse(parentCourse);

		return entity;
	}
}
