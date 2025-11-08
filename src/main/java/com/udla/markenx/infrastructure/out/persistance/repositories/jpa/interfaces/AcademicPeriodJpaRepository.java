package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicPeriodJpaEntity;

public interface AcademicPeriodJpaRepository extends JpaRepository<AcademicPeriodJpaEntity, Long> {

  /**
   * Checks if a period exists for the given year and semester.
   */
  boolean existsByYearAndSemesterNumber(int year, int semesterNumber);

  /**
   * Counts periods for a specific year.
   */
  long countByYear(int year);
}
