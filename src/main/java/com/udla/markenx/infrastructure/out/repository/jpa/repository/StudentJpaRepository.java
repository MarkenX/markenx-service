package com.udla.markenx.infrastructure.out.repository.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.repository.jpa.entity.StudentJpaEntity;

public interface StudentJpaRepository extends JpaRepository<StudentJpaEntity, Long> {

}
