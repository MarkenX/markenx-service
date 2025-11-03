package com.udla.markenx.infrastructure.outbound.persistance.jpa.mapper;

import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.infrastructure.outbound.persistance.jpa.entity.AttemptJpaEntity;

public class AttemptMapper {

  public static Attempt toDomain(AttemptJpaEntity entity) {
    if (entity == null)
      return null;

    Attempt attempt = new Attempt();
    attempt.setId(entity.getId());
    attempt.setScore(entity.getScore());
    attempt.setDate(entity.getDate());
    attempt.setDuration(entity.getDuration());

    return attempt;
  }
}
