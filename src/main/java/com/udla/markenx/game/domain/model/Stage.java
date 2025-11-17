package com.udla.markenx.game.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;
import com.udla.markenx.shared.domain.model.DomainBaseModel;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;

public class Stage extends DomainBaseModel {
  private static final Class<Stage> CLAZZ = Stage.class;

  private final String code;
  private final String name;
  private final String description;
  private final Double budget;
  private final Double scoreToWin;

  public Stage(
      UUID id,
      String code,
      DomainBaseModelStatus status,
      String name,
      String description,
      Double budget,
      Double scoreToWin,
      String createdBy,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super(id, code, status, createdBy, createdAt, updatedAt);
    this.name = requireName(name);
    this.description = description;
    this.budget = budget;
    this.scoreToWin = scoreToWin;
    this.code = generateCode();
  }

  // #region Getters

  public String getCode() {
    return this.code;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public Double getBudget() {
    return this.budget;
  }

  public Double getScoreToWin() {
    return this.scoreToWin;
  }

  // #endregion

  public String requireName(String name) {
    return EntityValidator.ensureNotNullOrEmpty(CLAZZ, name, "name");
  }

  @Override
  protected String generateCode() {
    return "";
  }
}
