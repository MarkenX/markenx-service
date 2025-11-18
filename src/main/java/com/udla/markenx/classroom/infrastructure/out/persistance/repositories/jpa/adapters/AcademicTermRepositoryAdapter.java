package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicPeriodRepositoryPort;
import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.AcademicTermJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.AcademicTermMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AcademicTermRepositoryAdapter implements AcademicPeriodRepositoryPort {

  private final AcademicTermJpaRepository jpaRepository;
  private final AcademicTermMapper mapper;

  @Override
  public AcademicTerm save(AcademicTerm newAcademicTerm) {
    // try {
    // AcademicTermJpaEntity entity = mapper.toEntity(newAcademicTerm);
    // AcademicTermJpaEntity saved = jpaRepository.save(entity);
    // return mapper.toDomain(saved);
    // } catch (DataAccessException e) {
    // throw new PersistenceException("Error al guardar el período académico", e);
    // }
    AcademicTermJpaEntity entity = mapper.toEntity(newAcademicTerm);
    AcademicTermJpaEntity saved = jpaRepository.save(entity);
    // return mapper.toDomain(saved);
    return newAcademicTerm;
  }

  @Override
  public AcademicTerm update(AcademicTerm existingAcademicTerm) {
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
    return jpaRepository.findAll(pageable)
        .map(entity -> entity.getStatus() == com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED
            ? mapper.toDomain(entity)
            : null)
        .map(domain -> domain);
  }

  @Override
  public Page<AcademicTerm> findAllIncludingDisabled(Pageable pageable) {
    return jpaRepository.findAll(pageable)
        .map(mapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.findAll().stream()
        .filter(entity -> entity.getExternalReference() != null &&
            entity.getExternalReference().getPublicId().equals(id))
        .findFirst()
        .ifPresent(entity -> jpaRepository.deleteById(entity.getId()));
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
        .map(mapper::toDomain)
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
