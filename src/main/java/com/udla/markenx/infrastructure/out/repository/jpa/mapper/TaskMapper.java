package com.udla.markenx.infrastructure.out.repository.jpa.mapper;

import com.udla.markenx.core.models.Task;
import com.udla.markenx.infrastructure.out.repository.jpa.entity.TaskJpaEntity;

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
}
