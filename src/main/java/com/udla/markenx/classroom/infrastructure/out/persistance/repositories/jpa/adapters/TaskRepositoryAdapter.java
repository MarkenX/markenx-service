package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.Objects;
import java.util.UUID;

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

	@Override
	public Optional<Task> findById(Long id) {
		Objects.requireNonNull(id, "Task ID cannot be null");
		return jpaRepository.findById(id)
				.filter(
						entity -> entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED)
				.map(mapper::toDomain);
	}

	@Override
	public Optional<Task> findByIdIncludingDisabled(Long id) {
		Objects.requireNonNull(id, "Task ID cannot be null");
		return jpaRepository.findById(id).map(mapper::toDomain);
	}

	@Override
	public Page<Task> findAll(Pageable pageable) {
		Objects.requireNonNull(pageable, "Pageable cannot be null");
		return jpaRepository.findAll(pageable)
				.map(entity -> entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED
						? mapper.toDomain(entity)
						: null)
				.map(domain -> domain);
	}

	@Override
	public Page<Task> findAllIncludingDisabled(Pageable pageable) {
		Objects.requireNonNull(pageable, "Pageable cannot be null");
		return jpaRepository.findAll(pageable).map(mapper::toDomain);
	}

	@Override
	public Optional<Task> findById(UUID id) {
		Objects.requireNonNull(id, "Task UUID cannot be null");
		return jpaRepository.findAll().stream()
				.filter(entity -> entity.getExternalReference() != null &&
						entity.getExternalReference().getPublicId().equals(id) &&
						entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED)
				.findFirst()
				.map(mapper::toDomain);
	}

	@Override
	public Optional<Task> findByIdIncludingDisabled(UUID id) {
		Objects.requireNonNull(id, "Task UUID cannot be null");
		return jpaRepository.findAll().stream()
				.filter(entity -> entity.getExternalReference() != null &&
						entity.getExternalReference().getPublicId().equals(id))
				.findFirst()
				.map(mapper::toDomain);
	}

	@Override
	public Page<Task> getTasksByCourseId(UUID courseId, Pageable pageable) {
		Objects.requireNonNull(courseId, "Course UUID cannot be null");
		return jpaRepository.findAll(pageable)
				.map(entity -> {
					if (entity.getCourse() != null &&
							entity.getCourse().getExternalReference() != null &&
							entity.getCourse().getExternalReference().getPublicId().equals(courseId) &&
							entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED) {
						return mapper.toDomain(entity);
					}
					return null;
				})
				.map(domain -> domain);
	}

	@Override
	public Page<Task> getCourseTasksByDueDate(UUID courseId, RangeDate rangeDate, Pageable pageable) {
		Objects.requireNonNull(courseId, "Course UUID cannot be null");
		Objects.requireNonNull(rangeDate, "RangeDate cannot be null");
		return jpaRepository.findAll(pageable)
				.map(entity -> {
					if (entity.getCourse() != null &&
							entity.getCourse().getExternalReference() != null &&
							entity.getCourse().getExternalReference().getPublicId().equals(courseId) &&
							entity.getDueDate() != null &&
							!entity.getDueDate().isBefore(rangeDate.getStartDate()) &&
							!entity.getDueDate().isAfter(rangeDate.getEndDate()) &&
							entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED) {
						return mapper.toDomain(entity);
					}
					return null;
				})
				.map(domain -> domain);
	}

}
