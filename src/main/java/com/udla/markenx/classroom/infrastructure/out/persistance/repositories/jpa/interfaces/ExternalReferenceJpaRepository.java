package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.ExternalReferenceJpaEntity;

public interface ExternalReferenceJpaRepository extends JpaRepository<ExternalReferenceJpaEntity, Long> {

}
