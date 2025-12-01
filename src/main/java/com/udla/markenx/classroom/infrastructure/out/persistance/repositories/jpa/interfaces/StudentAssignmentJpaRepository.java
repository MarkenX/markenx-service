package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentAssignmentJpaEntity;

public interface StudentAssignmentJpaRepository extends JpaRepository<StudentAssignmentJpaEntity, Long> {
  Optional<StudentAssignmentJpaEntity> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

  List<StudentAssignmentJpaEntity> findByStudentId(Long studentId);
}
