package com.udla.markenx.application.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.application.ports.out.persistance.repositories.AcademicPeriodRepositoryPort;
import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.exceptions.MaxPeriodsPerYearExceededException;
import com.udla.markenx.core.exceptions.PeriodHasCoursesException;
import com.udla.markenx.core.exceptions.ResourceNotFoundException;
import com.udla.markenx.core.models.AcademicTerm;
import com.udla.markenx.core.models.Course;
import com.udla.markenx.core.services.AcademicPeriodDomainService;

/**
 * Service for Academic Period operations.
 */
@Service
public class AcademicPeriodService {

  private final AcademicPeriodRepositoryPort periodRepository;

  public AcademicPeriodService(AcademicPeriodRepositoryPort periodRepository) {
    this.periodRepository = periodRepository;
  }

  /**
   * Creates a new academic period with automatic semester number assignment.
   */
  @Transactional
  public AcademicTerm createAcademicPeriod(LocalDate startDate, LocalDate endDate, int year) {
    // Validate dates using domain service
    AcademicPeriodDomainService.validateTermDates(startDate, endDate);

    // Check if we already have 2 periods for this year
    long periodsInYear = periodRepository.countByYear(year);
    if (periodsInYear >= 2) {
      throw new MaxPeriodsPerYearExceededException(year);
    }

    // Get existing periods for this year to determine semester number
    List<AcademicTerm> existingPeriodsInYear = periodRepository.findAllPeriods().stream()
        .filter(p -> p.getAcademicYear() == year)
        .toList();

    // Determine semester number using domain service
    int semesterNumber = AcademicPeriodDomainService.determineSemesterNumber(existingPeriodsInYear, startDate);

    // Check if this year/semester combination already exists
    if (periodRepository.existsByYearAndSemesterNumber(year, semesterNumber)) {
      throw new InvalidEntityException(
          AcademicTerm.class,
          String.format("Ya existe un período para el %s semestre del año %d",
              semesterNumber == 1 ? "primer" : "segundo", year));
    }

    // Create domain object (this validates dates and year match)
    AcademicTerm newPeriod = new AcademicTerm(startDate, endDate, year, semesterNumber);

    // Check for overlaps with existing periods using domain service
    List<AcademicTerm> allPeriods = periodRepository.findAllPeriods();
    AcademicPeriodDomainService.validateNoOverlaps(newPeriod, allPeriods, null);

    // Save and return
    return periodRepository.save(newPeriod);
  }

  /**
   * Updates an existing academic period (dates and year can be updated).
   */
  @Transactional
  public AcademicTerm updateAcademicPeriod(Long id, LocalDate startDate, LocalDate endDate, Integer year) {
    // Find existing period
    AcademicTerm existing = periodRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

    // Validate new dates using domain service
    if (startDate != null && endDate != null) {
      AcademicPeriodDomainService.validateTermDates(startDate, endDate);
    }

    // Update dates
    if (startDate != null) {
      existing.setStartOfTerm(startDate);
    }
    if (endDate != null) {
      existing.setEndOfTerm(endDate);
    }

    // Update year if provided
    if (year != null) {
      existing.setAcademicYear(year);
    }

    // Check for overlaps (excluding this period) using domain service
    List<AcademicTerm> allPeriods = periodRepository.findAllPeriods();
    AcademicPeriodDomainService.validateNoOverlaps(existing, allPeriods, id);

    // Save and return
    return periodRepository.update(existing);
  }

  /**
   * Retrieves an academic period by ID.
   */
  public AcademicTerm getAcademicPeriodById(Long id) {
    return periodRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
  }

  /**
   * Retrieves all academic periods with pagination.
   */
  public Page<AcademicTerm> getAllAcademicPeriods(Pageable pageable) {
    return periodRepository.findAll(pageable);
  }

  /**
   * Deletes an academic period by ID.
   * Fails if the period has associated courses.
   */
  @Transactional
  public void deleteAcademicPeriod(Long id) {
    // Check if period exists
    periodRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

    // Check if period has courses
    int coursesCount = periodRepository.countCoursesByPeriodId(id);
    if (coursesCount > 0) {
      throw new PeriodHasCoursesException(id, coursesCount);
    }

    // Delete
    periodRepository.deleteById(id);
  }

  /**
   * Retrieves all courses for a specific academic period.
   */
  public List<Course> getCoursesByPeriodId(Long periodId) {
    // Verify period exists
    periodRepository.findById(periodId)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", periodId));

    return periodRepository.findCoursesByPeriodId(periodId);
  }
}
