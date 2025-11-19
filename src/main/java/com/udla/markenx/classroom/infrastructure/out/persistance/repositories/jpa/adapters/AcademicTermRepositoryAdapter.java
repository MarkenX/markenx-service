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
    AcademicTermJpaEntity saved = jpaRepository.save(entity);

    // Generate code after persistence if it's a new entity
    if (newAcademicTerm.getCode() == null && saved.getId() != null) {
      String generatedCode = com.udla.markenx.classroom.domain.models.AcademicTerm.generateCodeFromData(
          saved.getAcademicYear(),
          saved.getStartOfTerm());
      saved.getExternalReference().setCode(generatedCode);
      saved = jpaRepository.save(saved);
    }

    return mapper.toDomain(saved);
  }

  @Override
  public AcademicTerm update(AcademicTerm existingAcademicTerm) {
    java.util.Objects.requireNonNull(existingAcademicTerm, "AcademicTerm cannot be null");

    // Find existing entity
    AcademicTermJpaEntity existingEntity = jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(existingAcademicTerm.getId()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("AcademicTerm not found with id: " + existingAcademicTerm.getId()));

    // Update fields
    existingEntity.setStartOfTerm(existingAcademicTerm.getStartOfTerm());
    existingEntity.setEndOfTerm(existingAcademicTerm.getEndOfTerm());
    existingEntity.setAcademicYear(existingAcademicTerm.getAcademicYear());
    existingEntity.setTermNumber(existingAcademicTerm.getTermNumber());
    existingEntity.setStatus(existingAcademicTerm.getStatus());
    existingEntity.setUpdatedBy(existingAcademicTerm.getUpdatedBy());
    existingEntity.setUpdatedAt(existingAcademicTerm.getUpdatedAtDateTime());

    AcademicTermJpaEntity saved = jpaRepository.save(existingEntity);
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
  public Page<AcademicTerm> findByStatus(DomainBaseModelStatus status, Pageable pageable) {
    java.util.Objects.requireNonNull(status, "Status cannot be null");
    java.util.Objects.requireNonNull(pageable, "Pageable cannot be null");
    return jpaRepository.findAll(pageable)
        .map(entity -> entity.getStatus() == status
            ? mapper.toDomainWithoutCourses(entity)
            : null)
        .map(domain -> domain);
  }

  @Override
  public void deleteById(UUID id) {
    java.util.Objects.requireNonNull(id, "ID cannot be null");
    jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id))
        .findFirst()
        .ifPresent(entity -> {
          // Soft delete: change status to DISABLED
          entity.setStatus(DomainBaseModelStatus.DISABLED);
          jpaRepository.save(entity);
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
        .map(entity -> entity.getCourses() != null
            ? (int) entity.getCourses().stream()
                .filter(course -> course.getStatus() == DomainBaseModelStatus.ENABLED)
                .count()
            : 0)
        .orElse(0);
  }

  @Override
  public List<Course> findCoursesByPeriodId(UUID periodId) {
    return java.util.List.of();
  }
}
