package com.udla.markenx.infrastructure.out.persistance.adapters.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.database.entities.PersonJpaEntity;

public interface PersonJpaRepository extends JpaRepository<PersonJpaEntity, Long> {

}
