package com.udla.markenx.adapters.outbound.persistance.jpa.mapper;

import com.udla.markenx.adapters.outbound.persistance.jpa.entity.AttemptJpaEntity;
import com.udla.markenx.core.model.Attempt;

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
