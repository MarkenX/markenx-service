package com.udla.markenx.classroom.application.ports.out.persistance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.domain.valueobjects.RangeDate;

public interface TaskRepositoryPort {

	Page<Task> getTasksByCourseId(Long courseId, Pageable pageable);

	Page<Task> getCourseTasksByDueDate(Long courseId, RangeDate rangeDate, Pageable pageable);

	// Page<Task> getCourseTasksByStatus(Long courseId, AssignmentStatus status,
	// Pageable pageable);

	// Page<Task> getCourseTasksByDueDateAndStatus(Long courseId, RangeDate
	// rangeDate, AssignmentStatus status,
	// Pageable pageable);

	Task createTask(Task task);

	Task getTaskById(Long taskId);
}
