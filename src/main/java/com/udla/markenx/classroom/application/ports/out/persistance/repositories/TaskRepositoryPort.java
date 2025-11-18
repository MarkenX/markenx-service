package com.udla.markenx.classroom.application.ports.out.persistance.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.domain.valueobjects.RangeDate;

public interface TaskRepositoryPort {

	Optional<Task> findById(Long id);

	Optional<Task> findByIdIncludingDisabled(Long id);

	Page<Task> findAll(Pageable pageable);

	Page<Task> findAllIncludingDisabled(Pageable pageable);

	Page<Task> getTasksByCourseId(Long courseId, Pageable pageable);

	Page<Task> getCourseTasksByDueDate(Long courseId, RangeDate rangeDate, Pageable pageable);
}
