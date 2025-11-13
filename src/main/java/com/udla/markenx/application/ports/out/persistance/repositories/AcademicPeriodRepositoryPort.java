package com.udla.markenx.application.ports.out.persistance.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udla.markenx.core.models.AcademicTerm;
import com.udla.markenx.core.models.Course;

/**
 * Repository port for Academic Period entity operations.
 */
public interface AcademicPeriodRepositoryPort {

  /**
   * Saves a new academic period.
   */
  AcademicTerm save(AcademicTerm period);

  /**
   * Updates an existing academic period.
   */
  AcademicTerm update(AcademicTerm period);

  /**
   * Finds an academic period by ID.
   */
  Optional<AcademicTerm> findById(Long id);

  /**
   * Finds all academic periods with pagination.
   */
  Page<AcademicTerm> findAll(Pageable pageable);

  /**
   * Deletes an academic period by ID.
   */
  void deleteById(Long id);

  /**
   * Checks if an academic period exists for a given year and semester.
   */
  boolean existsByYearAndSemesterNumber(int year, int semesterNumber);

  /**
   * Counts academic periods for a specific year.
   */
  long countByYear(int year);

  /**
   * Finds all academic periods (for overlap validation).
   */
  List<AcademicTerm> findAllPeriods();

  /**
   * Counts the number of courses in an academic period.
   */
  int countCoursesByPeriodId(Long periodId);

  /**
   * Finds all courses for a specific academic period.
   */
  List<Course> findCoursesByPeriodId(Long periodId);
}
