package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.interfaces.StudentAssignmentJpaEntity;

public interface StudentAssignmentJpaRepository extends JpaRepository<StudentAssignmentJpaEntity, Long> {
  Optional<StudentAssignmentJpaEntity> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);
}
