package com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.ExternalReferenceJpaEntity;

public interface ExternalReferenceJpaRepository extends JpaRepository<ExternalReferenceJpaEntity, Long> {

}
