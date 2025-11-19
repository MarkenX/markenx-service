package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort;
import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.domain.valueobjects.RangeDate;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.TaskJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.TaskMapper;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepositoryPort {
	private final TaskJpaRepository jpaRepository;
	private final TaskMapper mapper;
	private final com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentAssignmentJpaRepository studentAssignmentRepository;
	private final com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.CourseJpaRepository courseJpaRepository;

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

	@Override
	public Task save(Task task) {
		Objects.requireNonNull(task, "Task cannot be null");

		// Find course entity if task has a courseId
		com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity courseEntity = null;
		if (task.getCourseId() != null) {
			courseEntity = courseJpaRepository.findAll().stream()
					.filter(c -> c.getExternalReference() != null &&
							c.getExternalReference().getPublicId().equals(task.getCourseId()))
					.findFirst()
					.orElseThrow(
							() -> new IllegalArgumentException("Curso no encontrado con ID: " + task.getCourseId()));
		}

		var entity = mapper.toEntity(task, courseEntity);
		var savedEntity = jpaRepository.save(entity);
		return mapper.toDomain(savedEntity);
	}

	@Override
	public Task update(Task task) {
		Objects.requireNonNull(task, "Task cannot be null");
		Objects.requireNonNull(task.getId(), "Task ID cannot be null for update");

		// Find existing entity by UUID
		var existingEntity = jpaRepository.findAll().stream()
				.filter(entity -> entity.getExternalReference() != null &&
						entity.getExternalReference().getPublicId().equals(task.getId()))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + task.getId()));

		// Update entity with new values
		existingEntity.setStatus(task.getStatus());
		existingEntity.setTitle(task.getTitle());
		existingEntity.setSummary(task.getSummary());
		existingEntity.setDueDate(task.getDueDate());
		existingEntity.setMaxAttempts(task.getMaxAttempts());
		existingEntity.setMinScoreToPass(task.getMinScoreToPass());
		existingEntity.setUpdatedBy(task.getUpdatedBy());
		existingEntity.setUpdatedAt(task.getUpdatedAtDateTime());

		var updatedEntity = jpaRepository.save(existingEntity);
		return mapper.toDomain(updatedEntity);
	}

	@Override
	public boolean hasStudentTaskDependencies(UUID taskId) {
		Objects.requireNonNull(taskId, "Task UUID cannot be null");

		// Find the task entity by UUID to get its internal ID
		var taskEntity = jpaRepository.findAll().stream()
				.filter(entity -> entity.getExternalReference() != null &&
						entity.getExternalReference().getPublicId().equals(taskId))
				.findFirst()
				.orElse(null);

		if (taskEntity == null) {
			return false;
		}

		// Check if there are any StudentAssignment records for this task
		return studentAssignmentRepository.findAll().stream()
				.anyMatch(sa -> sa.getAssignment() != null &&
						sa.getAssignment().getId().equals(taskEntity.getId()));
	}

}
