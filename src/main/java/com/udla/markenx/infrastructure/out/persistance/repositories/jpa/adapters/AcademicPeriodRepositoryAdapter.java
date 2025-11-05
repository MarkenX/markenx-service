package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.udla.markenx.application.ports.out.persistance.repositories.AcademicPeriodRepositoryPort;
import com.udla.markenx.core.models.AcademicPeriod;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AcademicPeriodJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.interfaces.AcademicPeriodJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers.AcademicPeriodMapper;

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
      throw new PersistenceException("Error al guardar el periodo académico", e);
    }
  }
}
