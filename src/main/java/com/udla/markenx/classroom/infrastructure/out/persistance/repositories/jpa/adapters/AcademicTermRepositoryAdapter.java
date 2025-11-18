package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicTermRepositoryPort;
import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.AcademicTermJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.AcademicTermMapper;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AcademicTermRepositoryAdapter implements AcademicTermRepositoryPort {

  private final AcademicTermJpaRepository jpaRepository;
  private final AcademicTermMapper mapper;

  @Override
  public AcademicTerm save(AcademicTerm newAcademicTerm) {
    java.util.Objects.requireNonNull(newAcademicTerm, "AcademicTerm cannot be null");
    AcademicTermJpaEntity entity = mapper.toEntity(newAcademicTerm);
    jpaRepository.save(entity);
    return newAcademicTerm;
  }

  @Override
  public AcademicTerm update(AcademicTerm existingAcademicTerm) {
    java.util.Objects.requireNonNull(existingAcademicTerm, "AcademicTerm cannot be null");
    AcademicTermJpaEntity entity = mapper.toEntity(existingAcademicTerm);
    AcademicTermJpaEntity saved = jpaRepository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public Optional<AcademicTerm> findById(UUID id) {
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id) &&
            entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED)
        .findFirst()
        .map(mapper::toDomain);
  }

  @Override
  public Optional<AcademicTerm> findByIdIncludingDisabled(UUID id) {
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id))
        .findFirst()
        .map(mapper::toDomain);
  }

  @Override
  public Page<AcademicTerm> findAll(Pageable pageable) {
    java.util.Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findAll(pageable)
        .map(entity -> entity.getStatus() == DomainBaseModelStatus.ENABLED
            ? mapper.toDomainWithoutCourses(entity)
            : null)
        .map(domain -> domain);
  }

  @Override
  public Page<AcademicTerm> findAllIncludingDisabled(Pageable pageable) {
    java.util.Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findAll(pageable)
        .map(mapper::toDomainWithoutCourses);
  }

  @Override
  public void deleteById(UUID id) {
    java.util.Objects.requireNonNull(id, "ID cannot be null");
    jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id))
        .findFirst()
        .ifPresent(entity -> {
          Long entityId = entity.getId();
          if (entityId != null) {
            jpaRepository.deleteById(entityId);
          }
        });
  }

  @Override
  public boolean existsByYearAndSemesterNumber(int year, int semesterNumber) {
    return jpaRepository.findAll().stream()
        .anyMatch(entity -> entity.getAcademicYear() == year && entity.getTermNumber() == semesterNumber);
  }

  @Override
  public long countByYear(int year) {
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getAcademicYear() == year)
        .count();
  }

  @Override
  public List<AcademicTerm> findAllPeriods() {
    return jpaRepository.findAll().stream()
        .map(mapper::toDomainWithoutCourses)
        .toList();
  }

  @Override
  public int countCoursesByPeriodId(UUID periodId) {
    return jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(periodId))
        .findFirst()
        .map(entity -> entity.getCourses() != null ? entity.getCourses().size() : 0)
        .orElse(0);
  }

  @Override
  public List<Course> findCoursesByPeriodId(UUID periodId) {
    return java.util.List.of();
  }
}
