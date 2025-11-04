package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;

public class AttemptMapper {

  public static Attempt toDomain(AttemptJpaEntity entity) {
    if (entity == null)
      return null;

    Attempt attempt = new Attempt(
        entity.getScore(),
        entity.getDate(),
        entity.getDuration(),
        entity.getResult(),
        entity.getCurrentStatus());

    return attempt;
  }
}
