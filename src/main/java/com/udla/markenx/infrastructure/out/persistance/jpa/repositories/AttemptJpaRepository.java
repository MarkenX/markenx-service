package com.udla.markenx.infrastructure.out.persistance.jpa.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.jpa.entities.AttemptJpaEntity;

public interface AttemptJpaRepository extends JpaRepository<AttemptJpaEntity, Long> {
  Page<AttemptJpaEntity> findByTaskId(Long taskId, Pageable pageable);
}
