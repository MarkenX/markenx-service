package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.persistance.repositories.AcademicPeriodRepositoryPort;
import com.udla.markenx.core.models.AcademicPeriod;
import com.udla.markenx.core.models.Course;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicPeriodJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.AcademicPeriodJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.AcademicPeriodMapper;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.CourseMapper;

import jakarta.persistence.PersistenceException;

@Repository
public class AcademicPeriodRepositoryAdapter implements AcademicPeriodRepositoryPort {
  private final AcademicPeriodJpaRepository jpaRepository;

  public AcademicPeriodRepositoryAdapter(AcademicPeriodJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public AcademicPeriod save(AcademicPeriod period) {
    try {
      AcademicPeriodJpaEntity entity = AcademicPeriodMapper.toEntity(period);
      AcademicPeriodJpaEntity saved = jpaRepository.save(entity);
      return AcademicPeriodMapper.toDomain(saved);
    } catch (DataAccessException e) {
      throw new PersistenceException("Error al guardar el período académico", e);
    }
  }

  @Override
  public AcademicPeriod update(AcademicPeriod period) {
    try {
      AcademicPeriodJpaEntity existingEntity = jpaRepository.findById(period.getId())
          .orElseThrow(() -> new PersistenceException("Período académico no encontrado"));

      existingEntity.setStartDate(period.getStartDate());
      existingEntity.setEndDate(period.getEndDate());

      AcademicPeriodJpaEntity updated = jpaRepository.save(existingEntity);
      return AcademicPeriodMapper.toDomain(updated);
    } catch (DataAccessException e) {
      throw new PersistenceException("Error al actualizar el período académico", e);
    }
  }

  @Override
  public Optional<AcademicPeriod> findById(Long id) {
    return jpaRepository.findById(id)
        .map(AcademicPeriodMapper::toDomain);
  }

  @Override
  public Page<AcademicPeriod> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable)
        .map(AcademicPeriodMapper::toDomain);
  }

  @Override
  public void deleteById(Long id) {
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
  public List<AcademicPeriod> findAllPeriods() {
    return jpaRepository.findAll().stream()
        .map(AcademicPeriodMapper::toDomain)
        .toList();
  }

  @Override
  public int countCoursesByPeriodId(Long periodId) {
    return jpaRepository.findById(periodId)
        .map(entity -> entity.getCourses() != null ? entity.getCourses().size() : 0)
        .orElse(0);
  }

  @Override
  public List<Course> findCoursesByPeriodId(Long periodId) {
    return jpaRepository.findById(periodId)
        .map(entity -> entity.getCourses().stream()
            .map(CourseMapper::toDomain)
            .toList())
        .orElse(List.of());
  }
}
