package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;

public interface AcademicTermJpaRepository extends JpaRepository<AcademicTermJpaEntity, UUID> {
  boolean existsByYearAndSemesterNumber(int year, int semesterNumber);

  long countByYear(int year);
}
