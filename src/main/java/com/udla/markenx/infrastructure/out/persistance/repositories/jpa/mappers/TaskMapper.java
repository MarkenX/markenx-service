package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import java.util.List;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.core.models.Task;

import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

public final class TaskMapper {

	private TaskMapper() {
		throw new UtilityClassInstantiationException(getClass());
	}

	public static @NonNull Task toDomain(TaskJpaEntity entity) {
		if (entity == null) {
			throw new DomainMappingException();
		}

		List<Attempt> attempts = entity.getAttempts().stream()
				.map(AttemptMapper::toDomain)
				.toList();

		Task domain = new Task(
				entity.getId(),
				entity.getTitle(),
				entity.getSummary(),
				entity.getDueDate(),
				entity.getMaxAttempts(),
				entity.getActiveAttempt(),
				entity.getMinimumScoreToPass(),
				attempts,
				entity.getCreatedAt(),
				entity.getLastModifiedAt());

		return domain;
	}

	public static @NonNull TaskJpaEntity toEntity(Task domain) {
		if (domain == null) {
			throw new EntityMappingException();
		}

		TaskJpaEntity entity = new TaskJpaEntity();
		entity.setId(domain.getId());
		entity.setTitle(domain.getTitle());
		entity.setSummary(domain.getSummary());
		entity.setCurrentStatus(domain.getCurrentStatus());
		entity.setDueDate(domain.getDueDate());
		entity.setMaxAttempts(domain.getMaxAttempts());
		entity.setActiveAttempt(domain.getActiveAttempt());
		entity.setMinimumScoreToPass(domain.getMinScoreToPass());
		entity.setCreatedAt(domain.getCreatedDateTime());
		entity.setLastModifiedAt(domain.getUpdatedDateTime());

		List<AttemptJpaEntity> attempts = domain.getAttempts().stream()
				.map(AttemptMapper::toEntity)
				.peek(e -> e.setTask(entity))
				.toList();
		entity.setAttempts(attempts);

		return entity;
	}
}
