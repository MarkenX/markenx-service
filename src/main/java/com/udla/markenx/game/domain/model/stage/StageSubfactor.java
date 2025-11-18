package com.udla.markenx.game.domain.model.stage;

import java.util.UUID;

import com.udla.markenx.game.domain.model.consumer.Subfactor;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;

public class StageSubfactor {
  private static final Class<StageSubfactor> CLAZZ = StageSubfactor.class;

  private final UUID id;
  private final Subfactor subfactor;
  private final Double weight;
  private final boolean isVisible;

  public StageSubfactor(
      UUID id,
      Subfactor subfactor,
      Double weight,
      boolean isVisible) {
    this.id = requireId(id);
    this.subfactor = subfactor;
    this.weight = weight;
    this.isVisible = isVisible;
  }

  // #region Getters

  public UUID getId() {
    return this.id;
  }

  public Subfactor getSubfactor() {
    return this.subfactor;
  }

  public Double getWeight() {
    return this.weight;
  }

  public boolean getIsVisible() {
    return this.isVisible;
  }

  // #endregion

  public UUID requireId(UUID id) {
    return EntityValidator.ensureNotNull(CLAZZ, id, "id");
  }
}
