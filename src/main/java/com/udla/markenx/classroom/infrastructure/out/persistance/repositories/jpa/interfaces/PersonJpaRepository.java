package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.PersonJpaEntity;

public interface PersonJpaRepository extends JpaRepository<PersonJpaEntity, Long> {

}
