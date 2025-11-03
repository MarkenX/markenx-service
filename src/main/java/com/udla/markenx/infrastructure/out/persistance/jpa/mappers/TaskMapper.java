package com.udla.markenx.infrastructure.out.persistance.jpa.mappers;

import com.udla.markenx.core.models.Task;
import com.udla.markenx.infrastructure.out.persistance.jpa.entities.TaskJpaEntity;

public class TaskMapper {

	public static Task toDomain(TaskJpaEntity entity) {
		if (entity == null) {
			return null;
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

	public static TaskJpaEntity toEntity(Task domain) {
		if (domain == null) {
			return null;
		}

		return null;
	}
}
