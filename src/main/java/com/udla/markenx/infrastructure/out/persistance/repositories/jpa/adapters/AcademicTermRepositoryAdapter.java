package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.persistance.repositories.AcademicPeriodRepositoryPort;
import com.udla.markenx.core.models.AcademicTerm;
import com.udla.markenx.core.models.Course;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicTermJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.AcademicTermJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.AcademicTermMapper;

import jakarta.persistence.PersistenceException;

@Repository
public class AcademicTermRepositoryAdapter implements AcademicPeriodRepositoryPort {
  private final AcademicTermJpaRepository jpaRepository;

  public AcademicTermRepositoryAdapter(AcademicTermJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public AcademicTerm save(AcademicTerm newAcademicTerm) {
    try {
      AcademicTermJpaEntity entity = AcademicTermMapper.toEntity(newAcademicTerm);
      AcademicTermJpaEntity saved = jpaRepository.save(entity);
      return AcademicTermMapper.toDomain(saved);
    } catch (DataAccessException e) {
      throw new PersistenceException("Error al guardar el período académico", e);
    }
  }

  @Override
  public AcademicTerm update(AcademicTerm existingAcademicTerm) {
    try {
      AcademicTermJpaEntity existingEntity = jpaRepository.findById(existingAcademicTerm.getId())
          .orElseThrow(() -> new PersistenceException("Período académico no encontrado"));

      existingEntity.setStartOfTerm(existingAcademicTerm.getStartOfTerm());
      existingEntity.setEndOfTerm(existingAcademicTerm.getEndOfTerm());

      AcademicTermJpaEntity updated = jpaRepository.save(existingEntity);
      return AcademicTermMapper.toDomain(updated);
    } catch (DataAccessException e) {
      throw new PersistenceException("Error al actualizar el período académico", e);
    }
  }

  @Override
  public Optional<AcademicTerm> findById(UUID id) {
    return jpaRepository.findById(id)
        .map(AcademicTermMapper::toDomain);
  }

  @Override
  public Page<AcademicTerm> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable)
        .map(AcademicTermMapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }

  @Override
  public boolean existsByYearAndSemesterNumber(int year, int semesterNumber) {
    return jpaRepository.existsByYearAndSemesterNumber(year, semesterNumber);
  }

  @Override
  public long countByYear(int year) {
    return jpaRepository.countByYear(year);
  }

  @Override
  public List<AcademicTerm> findAllPeriods() {
    return jpaRepository.findAll().stream()
        .map(AcademicTermMapper::toDomain)
        .toList();
  }

  @Override
  public int countCoursesByPeriodId(UUID periodId) {
    // return jpaRepository.findById(periodId)
    // .map(entity -> entity.getCourses() != null ? entity.getCourses().size() : 0)
    // .orElse(0);
    return 0;
  }

  @Override
  public List<Course> findCoursesByPeriodId(UUID periodId) {
    // return jpaRepository.findById(periodId)
    // .map(entity -> entity.getCourses().stream()
    // .map(CourseMapper::toDomain)
    // .toList())
    // .orElse(List.of());
    return null;
  }
}
