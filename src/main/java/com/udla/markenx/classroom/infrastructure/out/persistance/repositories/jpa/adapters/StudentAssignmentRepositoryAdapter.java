package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.stereotype.Repository;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentAssignmentRepositoryPort;
import com.udla.markenx.classroom.domain.interfaces.Assignment;
import com.udla.markenx.classroom.domain.interfaces.StudentAssignment;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentAssignmentJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.StudentAssignmentMapper;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentAssignmentJpaEntity;

@Repository
@RequiredArgsConstructor
public class StudentAssignmentRepositoryAdapter implements StudentAssignmentRepositoryPort {
  private final StudentAssignmentJpaRepository jpaRepository;
  private final StudentAssignmentMapper mapper;

  @Override
  public StudentAssignment<? extends Assignment> getByAssignmentIdAndStudentId(Long assignmentId, Long studentId) {
    StudentAssignmentJpaEntity entity = jpaRepository.findByAssignmentIdAndStudentId(assignmentId, studentId)
        .orElse(null);
    if (entity == null) {
      return null;
    }
    StudentAssignment<? extends Assignment> domain = mapper.toDomain(entity);
    // load attempts (all) for this pair
    // Page<com.udla.markenx.core.models.Attempt> attempts = attemptRepositoryPort
    // .getAttemptsByStudentAssignmentId(entity.getId(), 0, 1000);
    // domain.setAttempts(attempts.getContent());
    return domain;
  }

  @Override
  public StudentAssignment<? extends Assignment> create(StudentAssignment<? extends Assignment> studentAssignment) {
    // if (studentAssignment == null) {
    // throw new IllegalArgumentException("StudentAssignment cannot be null");
    // }
    // java.util.Objects.requireNonNull(studentAssignment.getAssignmentId(),
    // "Assignment id cannot be null");
    // java.util.Objects.requireNonNull(studentAssignment.getStudent(), "Student id
    // cannot be null");

    // Long assignmentId = studentAssignment.getAssignmentId();
    // Long studentId = studentAssignment.getStudent();

    // com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity
    // assignmentEntity = assignmentJpaRepository
    // .findById(assignmentId)
    // .orElseThrow(() -> new IllegalArgumentException("Assignment not found with
    // id: " + assignmentId));
    // StudentJpaEntity studentEntity = studentJpaRepository.findById(studentId)
    // .orElseThrow(() -> new IllegalArgumentException("Student not found with id: "
    // + studentId));

    // StudentAssignmentJpaEntity entity =
    // StudentAssignmentMapper.toEntity(studentAssignment, assignmentEntity,
    // studentEntity);
    // StudentAssignmentJpaEntity saved = jpaRepository.save(entity);
    // StudentAssignment domain = StudentAssignmentMapper.toDomain(saved);
    // // no attempts on creation
    // return domain;
    return null;
  }
}
