package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.persistance.repositories.AttemptRepositoryPort;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.AttemptJpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.AttemptMapper;

import lombok.RequiredArgsConstructor;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentAssignmentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.StudentAssignmentJpaRepository;

@Repository
@RequiredArgsConstructor
public class AttemptRepositoryAdapter implements AttemptRepositoryPort {
  private final AttemptJpaRepository jpaRepository;
  private final StudentAssignmentJpaRepository studentAssignmentJpaRepository;
  private final AttemptMapper mapper;

  @Override
  public Page<Attempt> getAttemptsByStudentAssignmentId(Long studentAssignmentId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    // return jpaRepository.findByStudentAssignmentId(studentAssignmentId,
    // pageable).map(AttemptMapper::toDomain);
    return null;
  }

  @Override
  public Page<Attempt> getAttemptsByTaskId(Long taskId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    // return jpaRepository.findByStudentAssignment_Assignment_Id(taskId,
    // pageable).map(AttemptMapper::toDomain);
    return null;
  }

  @Override
  public Attempt createAttempt(Attempt attempt, Long studentAssignmentId) {
    if (attempt == null) {
      throw new IllegalArgumentException("Attempt cannot be null");
    }
    if (attempt.getId() != null) {
      throw new IllegalArgumentException("Cannot create attempt that already has an id");
    }
    if (studentAssignmentId == null) {
      throw new IllegalArgumentException("StudentAssignment id cannot be null");
    }

    // map domain to entity and attach associations
    // AttemptJpaEntity entity = mapper.toEntity(attempt);

    // StudentAssignmentJpaEntity saEntity =
    // studentAssignmentJpaRepository.findById(studentAssignmentId)
    // .orElseThrow(() -> new IllegalArgumentException("StudentAssignment not found
    // with id: " + studentAssignmentId));

    // entity.setStudentAssignment(saEntity);

    // AttemptJpaEntity saved = jpaRepository.save(entity);
    // return mapper.toDomain(saved);
    return null;
  }
}
