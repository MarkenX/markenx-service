package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;

public interface AcademicTermJpaRepository extends JpaRepository<AcademicTermJpaEntity, Long> {

  // boolean existsByYearAndSemesterNumber(int year, int semesterNumber);

  // long countByYear(int year);
}
