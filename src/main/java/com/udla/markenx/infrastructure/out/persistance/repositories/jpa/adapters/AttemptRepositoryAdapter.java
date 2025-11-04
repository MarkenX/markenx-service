package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.persistance.repositories.AttemptRepositoryPort;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.AttemptJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.AttemptMapper;

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
