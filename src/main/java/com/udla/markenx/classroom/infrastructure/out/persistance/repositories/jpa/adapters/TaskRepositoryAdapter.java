package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort;
import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.domain.valueobjects.RangeDate;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.TaskJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.TaskMapper;

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
	public Task save(Task task) {
		return null;
	}

	@Override
	public Task update(Task task) {
		return null;
	}

	@Override
	public Optional<Task> findById(Long id) {
		return jpaRepository.findById(id)
				.filter(
						entity -> entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED)
				.map(mapper::toDomain);
	}

	@Override
	public Optional<Task> findByIdIncludingDisabled(Long id) {
		return jpaRepository.findById(id).map(mapper::toDomain);
	}

	@Override
	public Page<Task> findAll(Pageable pageable) {
		return jpaRepository.findAll(pageable)
				.map(entity -> entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED
						? mapper.toDomain(entity)
						: null)
				.map(domain -> domain);
	}

	@Override
	public Page<Task> findAllIncludingDisabled(Pageable pageable) {
		return jpaRepository.findAll(pageable).map(mapper::toDomain);
	}

	@Override
	public void deleteById(Long id) {
	}

	@Override
	public Task createTask(Task task) {
		return null;
	}

	@Override
	public Task getTaskById(Long taskId) {
		return null;
	}
}
