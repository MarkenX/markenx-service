package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AuditJpaEntity;

public interface AuditJpaRepository extends JpaRepository<AuditJpaEntity, Long> {
  // List<AuditJpaEntity> findByTargetEntityIdOrderByPerformedAtAsc(Long
  // entityId);
}
