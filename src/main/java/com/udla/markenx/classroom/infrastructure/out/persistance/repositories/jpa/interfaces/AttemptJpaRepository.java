package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.util.UUID;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;

public interface AttemptJpaRepository extends JpaRepository<AttemptJpaEntity, Long> {
  // Page<AttemptJpaEntity> findByStudentAssignmentId(UUID studentAssignmentId,
  // Pageable pageable);

  // Page<AttemptJpaEntity> findByStudentAssignment_Assignment_Id(UUID
  // assignmentId, Pageable pageable);
}
