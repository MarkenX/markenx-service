package com.udla.markenx.adapters.outbound.persistance.jpa.mapper;

import com.udla.markenx.adapters.outbound.persistance.jpa.entity.TaskJpaEntity;
import com.udla.markenx.domain.model.Task;

public class TaskMapper {
    
    public static Task toDomain(TaskJpaEntity entity) {
        if (entity == null) return null;

        Task task = new Task();
        task.setId(entity.getId());
        task.setTitle(entity.getTitle());
        task.setSummnary(entity.getSummnary());
        task.setCurrentStatus(entity.getCurrentStatus());
        task.setActiveAttempt(entity.getActiveAttempt());
        task.setMaxAttempts(entity.getMaxAttempts());
        task.setDueDate(entity.getDueDate());

        return task;
    }
}
