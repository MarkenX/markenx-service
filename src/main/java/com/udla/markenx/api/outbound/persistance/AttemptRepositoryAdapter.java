package com.udla.markenx.api.outbound.persistance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.api.outbound.persistance.jpa.mapper.AttemptMapper;
import com.udla.markenx.api.outbound.persistance.jpa.repository.AttemptJpaRepository;
import com.udla.markenx.core.interfaces.out.AttemptRepositoryPort;
import com.udla.markenx.core.model.Attempt;

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
