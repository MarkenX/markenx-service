package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.persistance.repositories.StudentAssignmentRepositoryPort;
import com.udla.markenx.core.interfaces.StudentAssignment;
import com.udla.markenx.application.ports.out.persistance.repositories.AttemptRepositoryPort;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.StudentAssignmentJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.TaskJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.StudentJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.StudentAssignmentMapper;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentAssignmentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

import org.springframework.data.domain.Page;

@Repository
public class StudentAssignmentRepositoryAdapter implements StudentAssignmentRepositoryPort {
  private final StudentAssignmentJpaRepository jpaRepository;
  private final TaskJpaRepository assignmentJpaRepository;
  private final StudentJpaRepository studentJpaRepository;
  private final AttemptRepositoryPort attemptRepositoryPort;

  public StudentAssignmentRepositoryAdapter(StudentAssignmentJpaRepository jpaRepository,
      TaskJpaRepository assignmentJpaRepository, StudentJpaRepository studentJpaRepository,
      AttemptRepositoryPort attemptRepositoryPort) {
    this.jpaRepository = jpaRepository;
    this.assignmentJpaRepository = assignmentJpaRepository;
    this.studentJpaRepository = studentJpaRepository;
    this.attemptRepositoryPort = attemptRepositoryPort;
  }

  @Override
  public StudentAssignment getByAssignmentIdAndStudentId(Long assignmentId, Long studentId) {
    StudentAssignmentJpaEntity entity = jpaRepository.findByAssignmentIdAndStudentId(assignmentId, studentId)
        .orElse(null);
    if (entity == null) {
      return null;
    }
    StudentAssignment domain = StudentAssignmentMapper.toDomain(entity);
    // load attempts (all) for this pair
    Page<com.udla.markenx.core.models.Attempt> attempts = attemptRepositoryPort
        .getAttemptsByStudentAssignmentId(entity.getId(), 0, 1000);
    domain.setAttempts(attempts.getContent());
    return domain;
  }

  @Override
  @SuppressWarnings("null")
  public StudentAssignment create(StudentAssignment studentAssignment) {
    if (studentAssignment == null) {
      throw new IllegalArgumentException("StudentAssignment cannot be null");
    }
    java.util.Objects.requireNonNull(studentAssignment.getAssignmentId(), "Assignment id cannot be null");
    java.util.Objects.requireNonNull(studentAssignment.getStudent(), "Student id cannot be null");

    Long assignmentId = studentAssignment.getAssignmentId();
    Long studentId = studentAssignment.getStudent();

    com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity assignmentEntity = assignmentJpaRepository
        .findById(assignmentId)
        .orElseThrow(() -> new IllegalArgumentException("Assignment not found with id: " + assignmentId));
    StudentJpaEntity studentEntity = studentJpaRepository.findById(studentId)
        .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

    StudentAssignmentJpaEntity entity = StudentAssignmentMapper.toEntity(studentAssignment, assignmentEntity,
        studentEntity);
    StudentAssignmentJpaEntity saved = jpaRepository.save(entity);
    StudentAssignment domain = StudentAssignmentMapper.toDomain(saved);
    // no attempts on creation
    return domain;
  }
}
