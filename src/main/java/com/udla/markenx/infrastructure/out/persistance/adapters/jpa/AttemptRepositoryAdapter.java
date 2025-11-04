package com.udla.markenx.infrastructure.out.persistance.adapters.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.repositories.AttemptRepositoryPort;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.infrastructure.out.persistance.adapters.jpa.repositories.AttemptJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.database.mappers.AttemptMapper;

@Repository
public class AttemptRepositoryAdapter implements AttemptRepositoryPort {
  private final AttemptJpaRepository jpaRepository;

  public AttemptRepositoryAdapter(AttemptJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Page<Attempt> getAttemptsByTaskId(Long taskId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return jpaRepository.findByTaskId(taskId, pageable).map(AttemptMapper::toDomain);
  }
}
