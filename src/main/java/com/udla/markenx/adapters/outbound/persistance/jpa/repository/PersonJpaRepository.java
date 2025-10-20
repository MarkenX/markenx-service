package com.udla.markenx.adapters.outbound.persistance.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.adapters.outbound.persistance.jpa.entity.PersonJpaEntity;

public interface PersonJpaRepository extends JpaRepository<PersonJpaEntity, Long> {
    
}
