package com.udla.markenx.infrastructure.out.persistance.adapters.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.repositories.TaskRepositoryPort;
import com.udla.markenx.core.models.Task;
import com.udla.markenx.core.valueobjects.RangeDate;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.infrastructure.out.persistance.adapters.jpa.repositories.TaskJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.database.entities.TaskJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.database.utils.mappers.TaskMapper;

@Repository
public class TaskRepositoryAdapter implements TaskRepositoryPort {
	private final TaskJpaRepository jpaRepository;

	public TaskRepositoryAdapter(TaskJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Page<Task> getTasksByStudentId(Long studentId, Pageable pageable) {
		return jpaRepository.findByStudentId(studentId, pageable)
				.map(TaskMapper::toDomain);
	}

	@Override
	public Page<Task> getStudentTasksByDueDate(Long studentId, RangeDate rangeDate, Pageable pageable) {
		return jpaRepository
				.findByStudentIdAndDueDateBetween(studentId, rangeDate.getStartDate(), rangeDate.getEndDate(), pageable)
				.map(TaskMapper::toDomain);
	}

	@Override
	public Page<Task> getStudentTasksByStatus(Long studentId, AssignmentStatus status, Pageable pageable) {
		return jpaRepository.findByStudentIdAndCurrentStatus(studentId, status, pageable)
				.map(TaskMapper::toDomain);
	}

	@Override
	public Page<Task> getStudentTasksByDueDateAndStatus(Long studentId, RangeDate rangeDate,
			AssignmentStatus status, Pageable pageable) {
		return jpaRepository
				.findByStudentIdAndDueDateBetweenAndCurrentStatus(studentId, rangeDate.getStartDate(), rangeDate.getEndDate(),
						status, pageable)
				.map(TaskMapper::toDomain);
	}

	@Override
	public TaskJpaEntity createTask(Task task) {
		return jpaRepository.save(TaskMapper.toEntity(task));
	}
}
