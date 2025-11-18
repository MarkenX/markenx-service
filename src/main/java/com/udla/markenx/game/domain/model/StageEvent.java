package com.udla.markenx.game.domain.model;

import java.util.UUID;

import com.udla.markenx.game.domain.valueobject.StageEventCategory;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;

public class StageEvent {
  private static final Class<StageEvent> CLAZZ = StageEvent.class;

  private final UUID id;
  private final StageEventCategory category;
  private final String description;

  public StageEvent(UUID id, StageEventCategory category, String description) {
    this.id = id;
    this.category = category;
    this.description = description;
  }

  // #region Getters

  public UUID getId() {
    return this.id;
  }

  public StageEventCategory getCategory() {
    return this.category;
  }

  public String getDescription() {
    return this.description;
  }

  // #endregion

  // #region Validations

  public UUID requireId(UUID id) {
    return EntityValidator.ensureNotNull(CLAZZ, id, "id");
  }

  // #endregion
}
