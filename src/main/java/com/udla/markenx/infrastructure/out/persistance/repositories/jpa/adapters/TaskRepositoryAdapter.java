package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.persistance.repositories.TaskRepositoryPort;
import com.udla.markenx.core.models.Task;
import com.udla.markenx.core.valueobjects.RangeDate;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.TaskJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.TaskMapper;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepositoryPort {
	private final TaskJpaRepository jpaRepository;
	private final TaskMapper mapper;

	@Override
	public Page<Task> getTasksByCourseId(Long courseId, Pageable pageable) {
		return jpaRepository.findByCourseId(courseId, pageable)
				.map(mapper::toDomain);
	}

	@Override
	public Page<Task> getCourseTasksByDueDate(Long courseId, RangeDate rangeDate, Pageable pageable) {
		return jpaRepository
				.findByCourseIdAndDueDateBetween(courseId, rangeDate.getStartDate(),
						rangeDate.getEndDate(), pageable)
				.map(mapper::toDomain);
	}

	// @Override
	// public Page<Task> getCourseTasksByStatus(Long courseId, AssignmentStatus
	// status, Pageable pageable) {
	// return jpaRepository.findByCourseIdAndCurrentStatus(courseId, status,
	// pageable)
	// .map(mapper::toDomain);
	// }

	// @Override
	// public Page<Task> getCourseTasksByDueDateAndStatus(Long courseId, RangeDate
	// rangeDate,
	// AssignmentStatus status, Pageable pageable) {
	// return jpaRepository
	// .findByCourseIdAndDueDateBetweenAndCurrentStatus(courseId,
	// rangeDate.getStartDate(), rangeDate.getEndDate(),
	// status, pageable)
	// .map(mapper::toDomain);
	// }

	@Override
	public Task createTask(Task task) {
		// TaskJpaEntity entity = jpaRepository.save(mapper.toEntity(task));
		// return mapper.toDomain(entity);
		return null;
	}

	@Override
	public Task getTaskById(Long taskId) {
		// Optional<TaskJpaEntity> opt = jpaRepository.findById(taskId);
		// return opt.map(mapper::toDomain)
		// .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " +
		// taskId));
		return null;
	}
}
