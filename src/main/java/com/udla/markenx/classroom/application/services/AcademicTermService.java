package com.udla.markenx.classroom.application.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.commands.CreateAcademicTermCommand;
import com.udla.markenx.classroom.application.commands.UpdateAcademicTermCommand;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicTermRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.domain.factories.AcademicTermFactory;
import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.services.AcademicTermDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AcademicTermService {

  private final AcademicTermRepositoryPort repository;
  private final AcademicTermDomainService domainService;
  private final AcademicTermFactory factory;

  @Transactional
  public AcademicTerm createAcademicTerm(CreateAcademicTermCommand command) {

    List<AcademicTerm> existingTerms = repository.findByAcademicYear(command.academicYear());

    AcademicTerm newTerm = factory.create(
        command.startOfTerm(),
        command.endOfTerm(),
        command.academicYear(),
        command.createdBy(),
        existingTerms);

    return repository.save(newTerm);
  }

  @Transactional
  public AcademicTerm createHistoricalAcademicTerm(CreateAcademicTermCommand command) {

    List<AcademicTerm> existingTerms = repository.findByAcademicYear(command.academicYear());

    AcademicTerm newTerm = factory.createHistorical(
        command.startOfTerm(),
        command.endOfTerm(),
        command.academicYear(),
        command.createdBy(),
        existingTerms);

    return repository.save(newTerm);
  }

  @Transactional
  public AcademicTerm updateAcademicTerm(UpdateAcademicTermCommand command) {
    AcademicTerm existing = repository
        .findByIdIncludingDisabled(command.id())
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", command.id()));

    List<AcademicTerm> existingTerms = repository.findByAcademicYear(command.academicYear());

    domainService.validateUpdate(
        existing,
        command.startOfTerm(),
        command.endOfTerm(),
        command.academicYear(),
        existingTerms);

    return repository.update(existing);
  }

  @Transactional(readOnly = true)
  public AcademicTerm getAcademicTermById(UUID id) {
    if (isAdmin()) {
      return repository.findByIdIncludingDisabled(id)
          .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
    }
    return repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
  }

  @Transactional(readOnly = true)
  public Page<AcademicTerm> getAllAcademicTerms(Pageable pageable) {
    if (isAdmin()) {
      return repository.findAllIncludingDisabled(pageable);
    }
    return repository.findAllPaged(pageable);
  }

  @Transactional(readOnly = true)
  public Page<AcademicTerm> getAcademicPeriodsByStatus(
      com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus status, Pageable pageable) {
    return repository.findByStatus(status, pageable);
  }

  @Transactional
  public void disableAcademicTerm(UUID id) {
    AcademicTerm period = repository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

    // Verificar si el período académico tiene cursos habilitados
    int enabledCoursesCount = repository.countCoursesByTermId(id);
    if (enabledCoursesCount > 0) {
      throw new com.udla.markenx.classroom.domain.exceptions.PeriodHasCoursesException(id, enabledCoursesCount);
    }

    period.disable();
    period.markUpdated();
    repository.update(period);
  }

  @Transactional
  public void enableAcademicTerm(UUID id) {
    AcademicTerm period = repository.findByIdIncludingDisabled(id)
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));

    period.enable();
    period.markUpdated();
    repository.update(period);
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }
}
