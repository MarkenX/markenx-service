package com.udla.markenx.core.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.models.AcademicTerm;

public class AcademicPeriodDomainService {

  private AcademicPeriodDomainService() {
    throw new UtilityClassInstantiationException(AcademicPeriodDomainService.class);
  }

  public static int determineSemesterNumber(List<AcademicTerm> existingTermsInYear, LocalDate newTermStartDate) {
    if (existingTermsInYear == null || existingTermsInYear.isEmpty()) {
      return 1;
    }

    if (existingTermsInYear.size() == 1) {
      AcademicTerm existingTerm = existingTermsInYear.get(0);

      if (newTermStartDate.isBefore(existingTerm.getStartOfTerm())) {
        return 1;
      } else {
        return 2;
      }
    }

    throw new InvalidEntityException(
        AcademicTerm.class,
        "No se puede determinar el número de semestre: ya existen dos períodos para este año");
  }

  public static void validateTermDates(LocalDate startDate, LocalDate endDate) {
    LocalDate today = LocalDate.now();

    if (!startDate.isBefore(endDate)) {
      throw new InvalidEntityException(
          AcademicTerm.class,
          "La fecha de inicio debe ser anterior a la fecha de fin");
    }

    if (startDate.isBefore(today) && !endDate.isAfter(today)) {
      throw new InvalidEntityException(
          AcademicTerm.class,
          "Si la fecha de inicio está en el pasado, la fecha de fin debe estar en el futuro");
    }

    if (startDate.isAfter(today)) {
      LocalDate maxFutureDate = today.plusMonths(6);
      if (startDate.isAfter(maxFutureDate)) {
        throw new InvalidEntityException(
            AcademicTerm.class,
            "La fecha de inicio no puede estar más de 6 meses en el futuro");
      }
    }
  }

  public static void validateNoOverlaps(AcademicTerm newTerm, List<AcademicTerm> allExistingTerms,
      Long excludeId) {
    for (AcademicTerm existing : allExistingTerms) {
      if (excludeId != null && existing.getId() != null && existing.getId().equals(excludeId)) {
        continue;
      }

      if (newTerm.overlapsWith(existing)) {
        throw new InvalidEntityException(
            AcademicTerm.class,
            String.format("El período se solapa con el período existente: %s (%s a %s)",
                existing.getLabel(),
                existing.getStartOfTerm(),
                existing.getEndOfTerm()));
      }
    }
  }

  public static List<AcademicTerm> reassignTermNumbers(List<AcademicTerm> termsInYear) {
    if (termsInYear == null || termsInYear.isEmpty()) {
      return termsInYear;
    }

    List<AcademicTerm> sorted = termsInYear.stream()
        .sorted(Comparator.comparing(AcademicTerm::getStartOfTerm))
        .toList();

    return sorted;
  }
}
