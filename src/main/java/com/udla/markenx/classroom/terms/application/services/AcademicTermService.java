//package com.udla.markenx.classroom.terms.application.services;
//
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.udla.markenx.classroom.terms.application.commands.CreateCommand;
//import com.udla.markenx.classroom.terms.application.commands.UpdateCommand;
//import com.udla.markenx.classroom.terms.application.ports.out.persistance.repositories.TermRepositoryPort;
//import com.udla.markenx.classroom.terms.domain.model.Term;
//import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
//import com.udla.markenx.classroom.terms.application.factories.AcademicTermFactory;
//import com.udla.markenx.classroom.terms.domain.services.TermDomainService;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class AcademicTermService {
//
//  private final TermRepositoryPort repository;
//  private final TermDomainService domainService;
//  private final AcademicTermFactory factory;
//
//  @Transactional
//  public Term createAcademicTerm(CreateCommand command) {
//
//    List<Term> existingTerms = repository.findByAcademicYear(command.year());
//
//    Term newTerm = factory.create(
//        command.startDate(),
//        command.endDate(),
//        command.year(),
//        command.createdBy(),
//        existingTerms);
//
//    return repository.save(newTerm);
//  }
//
//  @Transactional
//  public Term createHistoricalAcademicTerm(CreateCommand command) {
//
//    List<Term> existingTerms = repository.findByAcademicYear(command.year());
//
//    Term newTerm = factory.createHistorical(
//        command.startDate(),
//        command.endDate(),
//        command.year(),
//        command.createdBy(),
//        existingTerms);
//
//    return repository.save(newTerm);
//  }
//
//  @Transactional
//  public Term updateAcademicTerm(UpdateCommand command) {
//    Term existing = repository
//        .findByIdIncludingDisabled(command.id())
//        .orElseThrow(() -> new ResourceNotFoundException("Período académico", command.id()));
//
//    List<Term> existingTerms = repository.findByAcademicYear(command.year());
//
//    domainService.validateUpdate(
//        existing,
//        command.startOfTerm(),
//        command.endOfTerm(),
//        command.year(),
//        existingTerms);
//
//    return repository.update(existing);
//  }
//
//  @Transactional(readOnly = true)
//  public Term getAcademicTermById(UUID id) {
//    if (isAdmin()) {
//      return repository.findByIdIncludingDisabled(id)
//          .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
//    }
//    return repository.findById(id)
//        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
//  }
//
//  @Transactional(readOnly = true)
//  public Page<Term> getAllAcademicTerms(Pageable pageable) {
//    if (isAdmin()) {
//      return repository.findAllIncludingDisabled(pageable);
//    }
//    return repository.findAllPaged(pageable);
//  }
//
//  @Transactional(readOnly = true)
//  public Page<Term> getAcademicPeriodsByStatus(
//      com.udla.markenx.shared.domain.valueobjects.EntityStatus status, Pageable pageable) {
//    return repository.findByStatus(status, pageable);
//  }
//
//  @Transactional
//  public void disableAcademicTerm(UUID id) {
//    Term period = repository.findByIdIncludingDisabled(id)
//        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
//
//    // Verificar si el período académico tiene cursos habilitados
//    int enabledCoursesCount = repository.countCoursesByTermId(id);
//    if (enabledCoursesCount > 0) {
//      throw new com.udla.markenx.classroom.domain.exceptions.PeriodHasCoursesException(id, enabledCoursesCount);
//    }
//
//    period.disable();
//    period.markUpdated();
//    repository.update(period);
//  }
//
//  @Transactional
//  public void enableAcademicTerm(UUID id) {
//    Term period = repository.findByIdIncludingDisabled(id)
//        .orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
//
//    period.enable();
//    period.markUpdated();
//    repository.update(period);
//  }
//
//  private boolean isAdmin() {
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    return authentication != null &&
//        authentication.getAuthorities().stream()
//            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//  }
//}
