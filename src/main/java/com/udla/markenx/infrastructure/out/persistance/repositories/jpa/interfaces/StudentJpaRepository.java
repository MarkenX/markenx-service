package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;

public interface StudentJpaRepository extends JpaRepository<StudentJpaEntity, Long> {

}
