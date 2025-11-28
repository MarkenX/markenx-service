package com.udla.markenx.classroom.domain.factories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.services.AcademicTermDomainService;

@Component
public class AcademicTermFactory {

  private final AcademicTermDomainService service;

  public AcademicTermFactory(AcademicTermDomainService service) {
    this.service = service;
  }

  public AcademicTerm create(LocalDate start, LocalDate end, int year, String createdBy,
      List<AcademicTerm> existingTerms) {

    service.validateDates(start, end);
    service.validateNoOverlaps(start, end, existingTerms);

    int termNumber = service.determineTermNumber(existingTerms, start);

    AcademicTerm term = AcademicTerm.createNew(start, end, year, termNumber, createdBy);

    term.regenerateCode();

    return term;
  }

  public AcademicTerm createHistorical(LocalDate start, LocalDate end, int year, String createdBy,
      List<AcademicTerm> existingTerms) {

    service.validateStartBeforeEnd(start, end);
    service.validateNoOverlaps(start, end, existingTerms);

    int termNumber = service.determineTermNumber(existingTerms, start);

    AcademicTerm term = AcademicTerm.createNew(start, end, year, termNumber, createdBy);

    term.regenerateCode();

    return term;
  }
}
