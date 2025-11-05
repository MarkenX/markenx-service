package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import org.springframework.lang.NonNull;

import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;
import com.udla.markenx.core.models.Attempt;

import com.udla.markenx.infrastructure.out.persistance.exceptions.DomainMappingException;
import com.udla.markenx.infrastructure.out.persistance.exceptions.EntityMappingException;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;

public final class AttemptMapper {

  private AttemptMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  public static @NonNull Attempt toDomain(AttemptJpaEntity entity) {
    if (entity == null) {
      throw new DomainMappingException();
    }

    Attempt domain = new Attempt(
        entity.getScore(),
        entity.getDate(),
        entity.getDuration(),
        entity.getResult(),
        entity.getCurrentStatus());

    return domain;
  }

  public static @NonNull AttemptJpaEntity toEntity(Attempt domain) {
    if (domain == null) {
      throw new EntityMappingException();
    }

    AttemptJpaEntity entity = new AttemptJpaEntity();
    entity.setId(domain.getId());
    entity.setScore(domain.getScore());
    entity.setDate(domain.getDate());
    entity.setDuration(domain.getDuration());
    entity.setResult(domain.getResult());
    entity.setCurrentStatus(domain.getCurrentStatus());

    return entity;
  }
}
