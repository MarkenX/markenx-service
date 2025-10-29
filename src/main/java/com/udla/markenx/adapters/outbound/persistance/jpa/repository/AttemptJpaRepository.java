package com.udla.markenx.adapters.outbound.persistance.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.adapters.outbound.persistance.jpa.entity.AttemptJpaEntity;

public interface AttemptJpaRepository extends JpaRepository<AttemptJpaEntity, Long> {
  Page<AttemptJpaEntity> findByTaskId(Long taskId, Pageable pageable);  
}
