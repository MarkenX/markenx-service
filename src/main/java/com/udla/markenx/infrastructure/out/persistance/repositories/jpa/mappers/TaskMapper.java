package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.models.Task;
import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

public class TaskMapper {

	public static @NonNull Task toDomain(TaskJpaEntity entity) {
		if (entity == null) {
			throw new DomainMappingException();
		}

		Task task = new Task(
				entity.getId(),
				entity.getTitle(),
				entity.getSummary(),
				entity.getDueDate(),
				entity.getMaxAttempts(),
				entity.getActiveAttempt(),
				entity.getMinimumScoreToPass());

		return task;
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

		return entity;
	}
}
