package com.udla.markenx.game.domain.model;

import java.util.UUID;

import com.udla.markenx.game.domain.valueobject.MainFactor;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;

public class Subfactor {
  private static final Class<Subfactor> CLAZZ = Subfactor.class;

  private final UUID id;
  private final MainFactor mainFactor;
  private final String description;

  public Subfactor(UUID id, MainFactor mainFactor, String description) {
    this.id = requireId(id);
    this.mainFactor = requireMainFactor(mainFactor);
    this.description = requireDescription(description);
  }

  // #region Getters

  public UUID getId() {
    return this.id;
  }

  public MainFactor getMainFactor() {
    return this.mainFactor;
  }

  public String getDescription() {
    return this.description;
  }

  // #endregion

  // #region Validations

  private UUID requireId(UUID id) {
    return EntityValidator.ensureNotNull(CLAZZ, id, "id");
  }

  private MainFactor requireMainFactor(MainFactor mainFactor) {
    return EntityValidator.ensureNotNull(CLAZZ, mainFactor, "mainFactor");
  }

  private String requireDescription(String description) {
    return EntityValidator.ensureNotNullOrEmpty(CLAZZ, description, "description");
  }

  // #endregion
}
