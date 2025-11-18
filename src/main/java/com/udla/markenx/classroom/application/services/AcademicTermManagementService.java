package com.udla.markenx.classroom.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.dtos.requests.CreateAcademicPeriodRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateAcademicTermRequestDTO;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicPeriodRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.services.AcademicPeriodDomainService;

@Service
public class AcademicTermManagementService {

  private final AcademicPeriodRepositoryPort periodRepository;

  public AcademicTermManagementService(AcademicPeriodRepositoryPort periodRepository) {
    this.periodRepository = periodRepository;
  }

  @Transactional
  public AcademicTerm createAcademicTerm(CreateAcademicPeriodRequestDTO request) {
    AcademicPeriodDomainService.validateTermDates(request.getStartDate(), request.getEndDate());

    List<AcademicTerm> existingPeriodsInYear = periodRepository.findAllPeriods().stream()
        .filter(p -> p.getAcademicYear() == request.getYear())
        .toList();

    int termNumber = AcademicPeriodDomainService.determineSemesterNumber(existingPeriodsInYear, request.getStartDate());

    if (periodRepository.existsByYearAndSemesterNumber(request.getYear(), termNumber)) {
      throw new InvalidEntityException(
          AcademicTerm.class,
          String.format("Ya existe un período para el %s semestre del año %d",
              termNumber == 1 ? "primer" : "segundo", request.getYear()));
    }

    AcademicTerm newPeriod = new AcademicTerm(request.getStartDate(), request.getEndDate(), request.getYear());

    return periodRepository.save(newPeriod);
  }

  @Transactional
  public AcademicTerm updateAcademicTerm(UUID id, UpdateAcademicTermRequestDTO request) {
    AcademicTerm existing = periodRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

    AcademicPeriodDomainService.validateTermDates(request.getStartDate(), request.getEndDate());

    existing.setStartOfTerm(request.getStartDate());
    existing.setEndOfTerm(request.getEndDate());
    existing.setAcademicYear(request.getYear());

    existing.markUpdated();
    return periodRepository.update(existing);
  }

  @Transactional(readOnly = true)
  public AcademicTerm getAcademicTermById(UUID id) {
    if (isAdmin()) {
      return periodRepository.findByIdIncludingDisabled(id)
          .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
    }
    return periodRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
  }

  @Transactional(readOnly = true)
  public Page<AcademicTerm> getAllAcademicTerms(Pageable pageable) {
    if (isAdmin()) {
      return periodRepository.findAllIncludingDisabled(pageable);
    }
    return periodRepository.findAll(pageable);
  }

  @Transactional
  public void deleteAcademicTerm(UUID id) {
    AcademicTerm period = periodRepository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

    period.disable();
    period.markUpdated();
    periodRepository.update(period);
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }
}
