package com.udla.markenx.game.domain.model.stage;

import java.time.LocalDateTime;
import java.util.UUID;

import com.udla.markenx.shared.domain.valueobjects.EntityStatus;
import com.udla.markenx.game.domain.model.consumer.ConsumerProfile;
import com.udla.markenx.shared.domain.model.DomainBaseModel;
import com.udla.markenx.shared.domain.util.EntityValidator;

public class Stage extends DomainBaseModel {
  private static final Class<Stage> CLAZZ = Stage.class;

  private final String code;
  private final String name;
  private final String description;
  private final Double budget;
  private final Double scoreToWin;
  private final ConsumerProfile consumerProfile;

  public Stage(
      UUID id,
      String code,
      EntityStatus status,
      String name,
      String description,
      Double budget,
      Double scoreToWin,
      ConsumerProfile consumerProfile,
      String createdBy,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super(id, code, status, createdBy, createdAt, updatedAt);
    this.name = requireName(name);
    this.description = description;
    this.budget = budget;
    this.scoreToWin = scoreToWin;
    this.consumerProfile = consumerProfile;
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

  public ConsumerProfile getConsumerProfile() {
    return this.consumerProfile;
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
