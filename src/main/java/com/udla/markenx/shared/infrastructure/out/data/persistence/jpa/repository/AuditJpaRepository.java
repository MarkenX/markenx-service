package com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.AuditJpaEntity;

public interface AuditJpaRepository extends JpaRepository<AuditJpaEntity, Long> {
  // List<AuditJpaEntity> findByTargetEntityIdOrderByPerformedAtAsc(Long
  // entityId);
}
