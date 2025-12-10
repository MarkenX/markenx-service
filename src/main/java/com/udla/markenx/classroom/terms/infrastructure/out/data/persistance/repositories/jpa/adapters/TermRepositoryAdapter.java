package com.udla.markenx.classroom.terms.infrastructure.out.data.persistance.repositories.jpa.adapters;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.classroom.terms.application.ports.out.persistance.repositories.TermRepositoryPort;
import com.udla.markenx.classroom.terms.domain.model.Term;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.terms.infrastructure.out.data.persistance.repositories.jpa.entities.TermJpaEntity;
import com.udla.markenx.classroom.terms.infrastructure.out.data.persistance.repositories.jpa.interfaces.TermJpaRepository;
import com.udla.markenx.classroom.terms.infrastructure.out.data.persistance.repositories.jpa.mappers.TermMapper;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TermRepositoryAdapter implements TermRepositoryPort {

  private final TermJpaRepository jpaRepository;
  private final TermMapper mapper;

  @Override
  public Term save(Term newTerm) {
    Objects.requireNonNull(newTerm, "AcademicTerm cannot be null");

    TermJpaEntity entity = mapper.toEntity(newTerm);

    @SuppressWarnings("null")
    TermJpaEntity saved = jpaRepository.save(entity);

    Term domain = mapper.toDomain(saved, false);
    saved.getExternalReference().setCode(domain.getCode());
    saved = jpaRepository.save(saved);

    return mapper.toDomain(saved, true);
  }

  @Override
  public Term update(Term existingTerm) {
    java.util.Objects.requireNonNull(existingTerm, "AcademicTerm cannot be null");

    // Find existing entity
    TermJpaEntity existingEntity = jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(existingTerm.getId()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("AcademicTerm not found with id: " + existingTerm.getId()));

    // Update fields
    existingEntity.setStartDate(existingTerm.getStartDate());
    existingEntity.setEndDate(existingTerm.getEndDate());
    existingEntity.setYear(existingTerm.getYear());
    existingEntity.setSequence(existingTerm.getSequence());
    existingEntity.setStatus(existingTerm.getStatus());
    existingEntity.setUpdatedBy(existingTerm.getUpdatedBy());
    existingEntity.setUpdatedAt(existingTerm.getUpdatedAtDateTime());

    TermJpaEntity saved = jpaRepository.save(existingEntity);
    return mapper.toDomain(saved, true);
  }

  @Override
  public Optional<Term> findById(UUID id) {
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id) &&
            entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.EntityStatus.ENABLED)
        .findFirst()
        .map(entity -> mapper.toDomain(entity, true));
  }

  @Override
  public Optional<Term> findByIdIncludingDisabled(UUID id) {
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id))
        .findFirst()
        .map(entity -> mapper.toDomain(entity, true));
  }

  @Override
  public Page<Term> findAllPaged(Pageable pageable) {
    java.util.Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findAll(pageable)
        .map(entity -> entity.getStatus() == EntityStatus.ENABLED
            ? mapper.toDomain(entity, false)
            : null)
        .map(domain -> domain);
  }

  @Override
  public Page<Term> findAllIncludingDisabled(Pageable pageable) {
    java.util.Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findAll(pageable)
        .map(entity -> mapper.toDomain(entity, false));
  }

  @Override
  public Page<Term> findByStatus(EntityStatus status, Pageable pageable) {
    Objects.requireNonNull(status, "Status cannot be null");
    Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findByStatus(status, pageable)
        .map(entity -> mapper.toDomain(entity, false));
  }

  @Override
  public List<Term> findByAcademicYear(int academicYear) {
    Objects.requireNonNull(academicYear, "Academic year cannot be null");
    return jpaRepository.findByAcademicYear(academicYear).stream()
        .map(entity -> mapper.toDomain(entity, false))
        .toList();
  }

  @Override
  public boolean existsByAcademicYearAndSemesterNumber(int year, int semesterNumber) {
    return jpaRepository.findAll().stream()
        .anyMatch(entity -> entity.getYear() == year && entity.getSequence() == semesterNumber);
  }

  @Override
  public long countByAcademicYear(int year) {
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getYear() == year)
        .count();
  }

  @Override
  public List<Term> findAll() {
    return jpaRepository.findAll().stream()
        .map(entity -> mapper.toDomain(entity, false))
        .toList();
  }

  @Override
  public int countCoursesByTermId(UUID periodId) {
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(periodId))
        .findFirst()
        .map(entity -> entity.getCourses() != null
            ? (int) entity.getCourses().stream()
                .filter(course -> course.getStatus() == EntityStatus.ENABLED)
                .count()
            : 0)
        .orElse(0);
  }

  @Override
  public List<Course> findCoursesByTermId(UUID periodId) {
    return java.util.List.of();
  }
}
