package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AuditJpaEntity;

public interface AuditJpaRepository extends JpaRepository<AuditJpaEntity, Long> {
  List<AuditJpaEntity> findByTargetEntityIdOrderByPerformedAtAsc(UUID entityId);
}
