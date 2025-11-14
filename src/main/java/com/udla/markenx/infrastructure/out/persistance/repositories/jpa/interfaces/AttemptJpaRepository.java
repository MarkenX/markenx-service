package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.util.UUID;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;

public interface AttemptJpaRepository extends JpaRepository<AttemptJpaEntity, UUID> {
  // Page<AttemptJpaEntity> findByStudentAssignmentId(UUID studentAssignmentId,
  // Pageable pageable);

  // Page<AttemptJpaEntity> findByStudentAssignment_Assignment_Id(UUID
  // assignmentId, Pageable pageable);
}
