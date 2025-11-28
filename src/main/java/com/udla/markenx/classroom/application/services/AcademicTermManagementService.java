package com.udla.markenx.classroom.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.dtos.requests.AcademicPeriod.CreateAcademicTermRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.AcademicPeriod.UpdateAcademicTermRequestDTO;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicTermRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.services.AcademicPeriodDomainService;

@Service
public class AcademicTermManagementService {

  private final AcademicTermRepositoryPort termRepository;

  public AcademicTermManagementService(AcademicTermRepositoryPort periodRepository) {
    this.termRepository = periodRepository;
  }

  @Transactional
  public AcademicTerm createAcademicTerm(CreateAcademicTermRequestDTO request) {
    AcademicPeriodDomainService.validateTermDates(request.getStartDate(), request.getEndDate());

    List<AcademicTerm> existingPeriodsInYear = termRepository.findAllPeriods().stream()
        .filter(p -> p.getAcademicYear() == request.getYear())
        .toList();

    int termNumber = AcademicPeriodDomainService.determineSemesterNumber(existingPeriodsInYear, request.getStartDate());

    if (termRepository.existsByYearAndSemesterNumber(request.getYear(), termNumber)) {
      throw new InvalidEntityException(
          AcademicTerm.class,
          String.format("Ya existe un período para el %s semestre del año %d",
              termNumber == 1 ? "primer" : "segundo", request.getYear()));
    }

    AcademicTerm newPeriod = new AcademicTerm(request.getStartDate(), request.getEndDate(), request.getYear());

    return termRepository.save(newPeriod);
  }

  @Transactional
  public AcademicTerm updateAcademicTerm(UUID id, UpdateAcademicTermRequestDTO request) {
    AcademicTerm existing = termRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

    AcademicPeriodDomainService.validateTermDates(request.getStartDate(), request.getEndDate());

    existing.setStartOfTerm(request.getStartDate());
    existing.setEndOfTerm(request.getEndDate());
    existing.setAcademicYear(request.getYear());

    existing.markUpdated();
    return termRepository.update(existing);
  }

  @Transactional(readOnly = true)
  public AcademicTerm getAcademicTermById(UUID id) {
    if (isAdmin()) {
      return termRepository.findByIdIncludingDisabled(id)
          .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
    }
    return termRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
  }

  @Transactional(readOnly = true)
  public Page<AcademicTerm> getAllAcademicTerms(Pageable pageable) {
    if (isAdmin()) {
      return termRepository.findAllIncludingDisabled(pageable);
    }
    return termRepository.findAll(pageable);
  }

  @Transactional(readOnly = true)
  public Page<AcademicTerm> getAcademicPeriodsByStatus(
      com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus status, Pageable pageable) {
    return termRepository.findByStatus(status, pageable);
  }

  @Transactional
  public void disableAcademicTerm(UUID id) {
    AcademicTerm period = termRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

    // Verificar si el período académico tiene cursos habilitados
    int enabledCoursesCount = termRepository.countCoursesByPeriodId(id);
    if (enabledCoursesCount > 0) {
      throw new com.udla.markenx.classroom.domain.exceptions.PeriodHasCoursesException(id, enabledCoursesCount);
    }

    period.disable();
    period.markUpdated();
    termRepository.update(period);
  }

  @Transactional
  public void enableAcademicTerm(UUID id) {
    AcademicTerm period = termRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

    period.enable();
    period.markUpdated();
    termRepository.update(period);
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }
}
