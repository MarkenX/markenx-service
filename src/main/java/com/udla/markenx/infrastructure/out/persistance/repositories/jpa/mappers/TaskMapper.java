package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Task;

import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class TaskMapper {

	private final EntityManager entityManager;

	public @NonNull Task toDomain(TaskJpaEntity entity) {
		if (entity == null) {
			throw new DomainMappingException();
		}

		UUID courseId = null;
		int academicTermYear = -1;
		if (entity.getCourse() != null) {
			courseId = entity.getCourse().getPublicId();
			academicTermYear = entity.getCourse().getAcademicTerm().getAcademicYear();
		}

		Task domain = new Task(
				entity.getPublicId(),
				entity.getCode(),
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

	public @NonNull TaskJpaEntity toEntity(Task domain) {
		if (domain == null) {
			throw new EntityMappingException();
		}

		TaskJpaEntity entity = new TaskJpaEntity();
		entity.setPublicId(domain.getId());
		entity.setCode(domain.getCode());
		entity.setId(domain.getSequence());
		entity.setStatus(domain.getStatus());
		entity.setTitle(domain.getTitle());
		entity.setSummary(domain.getSummary());
		entity.setDueDate(domain.getDueDate());
		entity.setMaxAttempts(domain.getMaxAttempts());
		entity.setMinScoreToPass(domain.getMinScoreToPass());
		entity.setCreatedBy(domain.getCreatedBy());
		entity.setCreatedAt(domain.getCreatedAtDateTime());
		entity.setUpdatedAt(domain.getUpdatedAtDateTime());

		if (domain.getCourseId() != null) {
			CourseJpaEntity ref = entityManager.getReference(CourseJpaEntity.class, domain.getCourseId());
			entity.setCourse(ref);
		}

		return entity;
	}
}
