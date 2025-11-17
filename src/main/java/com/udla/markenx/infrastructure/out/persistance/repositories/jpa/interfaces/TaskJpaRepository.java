package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

public interface TaskJpaRepository extends JpaRepository<TaskJpaEntity, Long> {
	Page<TaskJpaEntity> findByCourseId(Long courseId, Pageable pageable);

	Page<TaskJpaEntity> findByCourseIdAndCurrentStatus(Long courseId,
			AssignmentStatus currentStatus,
			Pageable pageable);

	Page<TaskJpaEntity> findByCourseIdAndDueDateBetween(Long courseId,
			LocalDate startDate, LocalDate endDate,
			Pageable pageable);

	Page<TaskJpaEntity> findByCourseIdAndDueDateBetweenAndCurrentStatus(Long courseId, LocalDate startDate,
			LocalDate endDate, AssignmentStatus currentStatus, Pageable pageable);
}
