package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

public interface TaskJpaRepository extends JpaRepository<TaskJpaEntity, Long> {
        Page<TaskJpaEntity> findByStudentId(Long studentId, Pageable pageable);

        Page<TaskJpaEntity> findByStudentIdAndCurrentStatus(Long studentId, AssignmentStatus currentStatus,
                        Pageable pageable);

        Page<TaskJpaEntity> findByStudentIdAndDueDateBetween(Long studentId, LocalDate startDate, LocalDate endDate,
                        Pageable pageable);

        Page<TaskJpaEntity> findByStudentIdAndDueDateBetweenAndCurrentStatus(Long studentId, LocalDate startDate,
                        LocalDate endDate, AssignmentStatus currentStatus, Pageable pageable);
}
