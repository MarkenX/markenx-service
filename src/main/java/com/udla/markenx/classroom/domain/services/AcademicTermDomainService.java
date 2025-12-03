package com.udla.markenx.classroom.domain.services;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.academicterms.domain.model.AcademicTerm;
import com.udla.markenx.classroom.domain.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.domain.exceptions.MaxAcademicTermsReachedException;

@Component
public class AcademicTermDomainService {

  public int validateUpdate(
      AcademicTerm existing,
      LocalDate start,
      LocalDate end,
      int year,
      List<AcademicTerm> existingTerms) {

    validateDates(start, end);

    List<AcademicTerm> others = existingTerms == null
        ? List.of()
        : existingTerms.stream()
            .filter(t -> !t.getId().equals(existing.getId()))
            .toList();

    validateNoOverlaps(start, end, others);

    return determineTermNumber(others, start);
  }

  public int determineTermNumber(List<AcademicTerm> existingTerms, LocalDate newStartDate) {
    if (existingTerms == null || existingTerms.isEmpty()) {
      return 1;
    }

    if (existingTerms.size() == 1) {
      AcademicTerm other = existingTerms.get(0);
      return newStartDate.isBefore(other.getTermStartDate()) ? 1 : 2;
    }

    throw new MaxAcademicTermsReachedException(newStartDate.getYear());
  }

  public void validateDates(LocalDate start, LocalDate end) {
    LocalDate today = LocalDate.now();

    validateStartBeforeEnd(start, end);
    validateStartNotTooFar(start, today);
    validatePastStartRequiresFutureEnd(start, end, today);
  }

  public void validateStartBeforeEnd(LocalDate start, LocalDate end) {
    if (!start.isBefore(end)) {
      throw new InvalidEntityException(
          AcademicTerm.class,
          "La fecha de inicio debe ser anterior a la fecha de fin");
    }
  }

  public void validatePastStartRequiresFutureEnd(LocalDate start, LocalDate end, LocalDate today) {
    if (start.isBefore(today) && !end.isAfter(today)) {
      throw new InvalidEntityException(
          AcademicTerm.class,
          "Si el período ya inició, su fecha de fin debe estar en el futuro");
    }
  }

  public void validateStartNotTooFar(LocalDate start, LocalDate today) {
    if (start.isAfter(today.plusMonths(6))) {
      throw new InvalidEntityException(
          AcademicTerm.class,
          "La fecha de inicio no puede estar a más de 6 meses en el futuro");
    }
  }

  public void validateNoOverlaps(LocalDate newStart, LocalDate newEnd, List<AcademicTerm> existing) {
    for (AcademicTerm t : existing) {
      if (!newStart.isAfter(t.getTermEndDate()) && !newEnd.isBefore(t.getTermStartDate())) {
        throw new InvalidEntityException(
            AcademicTerm.class,
            String.format("Se solapa con el período existente %s (%s a %s)",
                t.getLabel(), t.getTermStartDate(), t.getTermEndDate()));
      }
    }
  }

  public List<AcademicTerm> reorderTerms(List<AcademicTerm> terms) {
    if (terms == null || terms.isEmpty())
      return terms;

    return terms.stream()
        .sorted(Comparator.comparing(AcademicTerm::getTermStartDate))
        .toList();
  }
}
