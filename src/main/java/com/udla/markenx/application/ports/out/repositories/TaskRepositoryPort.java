package com.udla.markenx.application.ports.out.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.core.models.Task;

import com.udla.markenx.core.valueobjects.RangeDate;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.infrastructure.out.persistance.jpa.entities.TaskJpaEntity;

public interface TaskRepositoryPort {
	Page<Task> getTasksByStudentId(Long studentId, Pageable pageable);

	Page<Task> getStudentTasksByDueDate(Long studentId, RangeDate rangeDate, Pageable pageable);

	Page<Task> getStudentTasksByStatus(Long studentId, AssignmentStatus status, Pageable pageable);

	Page<Task> getStudentTasksByDueDateAndStatus(Long studentId, RangeDate rangeDate, AssignmentStatus status,
			Pageable pageable);

	TaskJpaEntity createTask(Task task);
}
