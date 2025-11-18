package com.udla.markenx.classroom.application.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicTermRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.services.AcademicPeriodDomainService;

@Service
public class AcademicTermService {

  private final AcademicTermRepositoryPort periodRepository;

  public AcademicTermService(AcademicTermRepositoryPort periodRepository) {
    this.periodRepository = periodRepository;
  }

  @Transactional
  public AcademicTerm createAcademicPeriod(LocalDate startOfTerm, LocalDate endOfTerm, int academicYear) {
    AcademicPeriodDomainService.validateTermDates(startOfTerm, endOfTerm);

    List<AcademicTerm> existingPeriodsInYear = periodRepository.findAllPeriods().stream()
        .filter(p -> p.getAcademicYear() == academicYear)
        .toList();

    // Determine semester number using domain service
    int termNumber = AcademicPeriodDomainService.determineSemesterNumber(existingPeriodsInYear, startOfTerm);

    // Check if this year/semester combination already exists
    if (periodRepository.existsByYearAndSemesterNumber(academicYear, termNumber)) {
      throw new InvalidEntityException(
          AcademicTerm.class,
          String.format("Ya existe un período para el %s semestre del año %d",
              termNumber == 1 ? "primer" : "segundo", academicYear));
    }

    // Create domain object (this validates dates and year match)
    AcademicTerm newPeriod = new AcademicTerm(startOfTerm, endOfTerm, academicYear, "");

    // Check for overlaps with existing periods using domain service
    List<AcademicTerm> allPeriods = periodRepository.findAllPeriods();
    AcademicPeriodDomainService.validateNoOverlaps(newPeriod, allPeriods, null);

    // Save and return
    return periodRepository.save(newPeriod);
  }

  /**
   * Updates an existing academic period (dates and year can be updated).
   */
  // @Transactional
  // public AcademicTerm updateAcademicPeriod(Long id, LocalDate startDate,
  // LocalDate endDate, Integer year) {
  // // Find existing period
  // AcademicTerm existing = periodRepository.findById(id)
  // .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

  // // Validate new dates using domain service
  // if (startDate != null && endDate != null) {
  // AcademicPeriodDomainService.validateTermDates(startDate, endDate);
  // }

  // // Update dates
  // if (startDate != null) {
  // existing.setStartOfTerm(startDate);
  // }
  // if (endDate != null) {
  // existing.setEndOfTerm(endDate);
  // }

  // // Update year if provided
  // if (year != null) {
  // existing.setAcademicYear(year);
  // }

  // // Check for overlaps (excluding this period) using domain service
  // List<AcademicTerm> allPeriods = periodRepository.findAllPeriods();
  // AcademicPeriodDomainService.validateNoOverlaps(existing, allPeriods, id);

  // // Save and return
  // return periodRepository.update(existing);
  // }

  /**
   * Retrieves an academic period by ID.
   */
  // public AcademicTerm getAcademicPeriodById(Long id) {
  // return periodRepository.findById(id)
  // .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
  // }

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
  // @Transactional
  // public void deleteAcademicPeriod(Long id) {
  // // Check if period exists
  // periodRepository.findById(id)
  // .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

  // // Check if period has courses
  // int coursesCount = periodRepository.countCoursesByPeriodId(id);
  // if (coursesCount > 0) {
  // throw new PeriodHasCoursesException(id, coursesCount);
  // }

  // // Delete
  // periodRepository.deleteById(id);
  // }

  /**
   * Retrieves all courses for a specific academic period.
   */
  // public List<Course> getCoursesByPeriodId(Long periodId) {
  // // Verify period exists
  // periodRepository.findById(periodId)
  // .orElseThrow(() -> new ResourceNotFoundException("Período académico",
  // periodId));

  // return periodRepository.findCoursesByPeriodId(periodId);
  // }
}
