package com.udla.markenx.infrastructure.out.persistance.database.utils.mappers;

import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.infrastructure.out.persistance.database.entities.AttemptJpaEntity;

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
