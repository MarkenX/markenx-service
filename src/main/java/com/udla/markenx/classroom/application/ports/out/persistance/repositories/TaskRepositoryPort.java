package com.udla.markenx.classroom.application.ports.out.persistance.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.domain.valueobjects.RangeDate;

public interface TaskRepositoryPort {

	Optional<Task> findById(Long id);

	Optional<Task> findByIdIncludingDisabled(Long id);

	Optional<Task> findById(UUID id);

	Optional<Task> findByIdIncludingDisabled(UUID id);

	Page<Task> findAll(Pageable pageable);

	Page<Task> findAllIncludingDisabled(Pageable pageable);

	Page<Task> findByStatus(com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus status, Pageable pageable);

	Page<Task> getTasksByCourseId(Long courseId, Pageable pageable);

	Page<Task> getTasksByCourseId(UUID courseId, Pageable pageable);

	Page<Task> getCourseTasksByDueDate(Long courseId, RangeDate rangeDate, Pageable pageable);

	Page<Task> getCourseTasksByDueDate(UUID courseId, RangeDate rangeDate, Pageable pageable);

	java.util.List<Task> findByCourseId(UUID courseId);

	Task save(Task task);

	Task update(Task task);

	boolean hasStudentTaskDependencies(UUID taskId);
}
