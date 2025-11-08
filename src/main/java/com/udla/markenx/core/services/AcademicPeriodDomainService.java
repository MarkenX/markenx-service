package com.udla.markenx.core.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.models.AcademicPeriod;

/**
 * Domain service for Academic Period business logic.
 * 
 * This service contains pure domain logic that doesn't fit naturally into
 * the AcademicPeriod entity itself, particularly logic that requires
 * multiple periods or external validation.
 */
public class AcademicPeriodDomainService {

  private AcademicPeriodDomainService() {
    // Utility class - prevent instantiation
  }

  /**
   * Determines the semester number for a new period based on existing periods.
   * 
   * Rules:
   * - If no periods exist for the year, assign semester 1
   * - If one period exists, compare start dates:
   * - If new period starts before existing: new = 1, existing should be 2
   * - If new period starts after existing: existing is 1, new = 2
   * - If two periods exist, this should never be called (validation elsewhere)
   * 
   * @param existingPeriodsInYear list of periods already in the year
   * @param newPeriodStartDate    start date of the new period
   * @return 1 or 2
   */
  public static int determineSemesterNumber(List<AcademicPeriod> existingPeriodsInYear, LocalDate newPeriodStartDate) {
    if (existingPeriodsInYear == null || existingPeriodsInYear.isEmpty()) {
      // First period of the year - always assign semester 1
      return 1;
    }

    if (existingPeriodsInYear.size() == 1) {
      AcademicPeriod existingPeriod = existingPeriodsInYear.get(0);

      if (newPeriodStartDate.isBefore(existingPeriod.getStartDate())) {
        // New period starts before the existing one, so it's semester 1
        return 1;
      } else {
        // New period starts after the existing one, so it's semester 2
        return 2;
      }
    }

    // This shouldn't happen - should be validated before calling this method
    throw new InvalidEntityException(
        AcademicPeriod.class,
        "No se puede determinar el número de semestre: ya existen dos períodos para este año");
  }

  /**
   * Validates date logic for academic periods.
   * 
   * Rules:
   * - startDate must be before endDate
   * - If startDate is in the past, endDate must be in the future
   * - If startDate is in the future, it cannot be more than 6 months ahead
   * 
   * @param startDate the start date to validate
   * @param endDate   the end date to validate
   * @throws InvalidEntityException if validation fails
   */
  public static void validatePeriodDates(LocalDate startDate, LocalDate endDate) {
    LocalDate today = LocalDate.now();

    // Basic validation: start before end
    if (!startDate.isBefore(endDate)) {
      throw new InvalidEntityException(
          AcademicPeriod.class,
          "La fecha de inicio debe ser anterior a la fecha de fin");
    }

    // If start date is in the past, end date must be in the future
    if (startDate.isBefore(today) && !endDate.isAfter(today)) {
      throw new InvalidEntityException(
          AcademicPeriod.class,
          "Si la fecha de inicio está en el pasado, la fecha de fin debe estar en el futuro");
    }

    // If start date is in the future, max 6 months ahead
    if (startDate.isAfter(today)) {
      LocalDate maxFutureDate = today.plusMonths(6);
      if (startDate.isAfter(maxFutureDate)) {
        throw new InvalidEntityException(
            AcademicPeriod.class,
            "La fecha de inicio no puede estar más de 6 meses en el futuro");
      }
    }
  }

  /**
   * Validates that a period doesn't overlap with any existing periods.
   * Uses domain logic from AcademicPeriod.overlapsWith().
   * 
   * @param newPeriod          the period to validate
   * @param allExistingPeriods all existing periods to check against
   * @param excludeId          optional ID to exclude from validation (for
   *                           updates)
   * @throws InvalidEntityException if overlap is detected
   */
  public static void validateNoOverlaps(AcademicPeriod newPeriod, List<AcademicPeriod> allExistingPeriods,
      Long excludeId) {
    for (AcademicPeriod existing : allExistingPeriods) {
      // Skip the period being updated
      if (excludeId != null && existing.getId() != null && existing.getId().equals(excludeId)) {
        continue;
      }

      // Check for overlap using domain logic
      if (newPeriod.overlapsWith(existing)) {
        throw new InvalidEntityException(
            AcademicPeriod.class,
            String.format("El período se solapa con el período existente: %s (%s a %s)",
                existing.getLabel(),
                existing.getStartDate(),
                existing.getEndDate()));
      }
    }
  }

  /**
   * Reassigns semester numbers for all periods in a year based on start dates.
   * The period that starts first gets semester 1, the second gets semester 2.
   * 
   * This is useful when updating periods and their order might change.
   * 
   * @param periodsInYear all periods for a specific year
   * @return list of periods with corrected semester numbers
   */
  public static List<AcademicPeriod> reassignSemesterNumbers(List<AcademicPeriod> periodsInYear) {
    if (periodsInYear == null || periodsInYear.isEmpty()) {
      return periodsInYear;
    }

    // Sort by start date
    List<AcademicPeriod> sorted = periodsInYear.stream()
        .sorted(Comparator.comparing(AcademicPeriod::getStartDate))
        .toList();

    // Note: This returns the list but doesn't modify the periods
    // The caller should handle semester number updates if needed
    return sorted;
  }
}
