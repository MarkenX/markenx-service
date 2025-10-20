package com.udla.markenx.adapters.outbound.persistance.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.adapters.outbound.persistance.jpa.entity.StudentJpaEntity;

public interface StudentJpaRepository extends JpaRepository<StudentJpaEntity, Long> {
    
}
