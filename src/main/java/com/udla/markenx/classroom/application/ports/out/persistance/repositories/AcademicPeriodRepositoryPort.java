package com.udla.markenx.classroom.application.ports.out.persistance.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.models.Course;

public interface AcademicPeriodRepositoryPort {

  AcademicTerm save(AcademicTerm period);

  AcademicTerm update(AcademicTerm period);

  Optional<AcademicTerm> findById(UUID id);

  Optional<AcademicTerm> findByIdIncludingDisabled(UUID id);

  Page<AcademicTerm> findAll(Pageable pageable);

  Page<AcademicTerm> findAllIncludingDisabled(Pageable pageable);

  void deleteById(UUID id);

  boolean existsByYearAndSemesterNumber(int year, int semesterNumber);

  long countByYear(int year);

  List<AcademicTerm> findAllPeriods();

  int countCoursesByPeriodId(UUID periodId);

  List<Course> findCoursesByPeriodId(UUID periodId);
}
