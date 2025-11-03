package com.udla.markenx.api.outbound.persistance.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.api.outbound.persistance.jpa.entity.StudentJpaEntity;

public interface StudentJpaRepository extends JpaRepository<StudentJpaEntity, Long> {

}
