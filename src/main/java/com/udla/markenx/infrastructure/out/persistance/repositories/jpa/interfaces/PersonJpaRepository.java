package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.interfaces.PersonJpaEntity;

public interface PersonJpaRepository extends JpaRepository<PersonJpaEntity, Long> {

}
