package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;

public interface AttemptJpaRepository extends JpaRepository<AttemptJpaEntity, Long> {
  Page<AttemptJpaEntity> findByStudentAssignmentId(Long studentAssignmentId, Pageable pageable);

  Page<AttemptJpaEntity> findByStudentAssignment_Assignment_Id(Long assignmentId, Pageable pageable);
}
