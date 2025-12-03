package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

public interface TaskJpaRepository extends JpaRepository<TaskJpaEntity, Long> {
	Page<TaskJpaEntity> findByCourseId(Long courseId, Pageable pageable);

	Page<TaskJpaEntity> findByStatus(EntityStatus status, Pageable pageable);

	// Page<TaskJpaEntity> findByCourseIdAndCurrentStatus(Long courseId,
	// AssignmentStatus currentStatus,
	// Pageable pageable);

	Page<TaskJpaEntity> findByCourseIdAndDueDateBetween(Long courseId,
			LocalDate startDate, LocalDate endDate,
			Pageable pageable);

	// Page<TaskJpaEntity> findByCourseIdAndDueDateBetweenAndCurrentStatus(Long
	// courseId, LocalDate startDate,
	// LocalDate endDate, AssignmentStatus currentStatus, Pageable pageable);
}
