package com.udla.markenx.infrastructure.out.persistance.database.mappers;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.models.Task;
import com.udla.markenx.infrastructure.out.persistance.database.entities.TaskJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.exceptions.MappingException;

public class TaskMapper {

	public static @NonNull Task toDomain(TaskJpaEntity entity) {
		if (entity == null) {
			throw new MappingException("No se puede mapear una entidad JPA nula a un objeto de dominio.");
		}

		Task task = new Task(
				entity.getId(),
				entity.getTitle(),
				entity.getSummary(),
				entity.getDueDate(),
				entity.getMaxAttempts(),
				entity.getActiveAttempt());

		return task;
	}

	public static @NonNull TaskJpaEntity toEntity(Task domain) {
		if (domain == null) {
			throw new MappingException("No se puede mapear un objeto nulo de dominio a entidad JPA.");
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
