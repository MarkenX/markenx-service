package com.udla.markenx.game.domain.model;

import java.util.UUID;

import com.udla.markenx.game.domain.valueobject.ActionCategory;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;

public class PlayerAction {
  private static final Class<PlayerAction> CLAZZ = PlayerAction.class;

  private final UUID id;
  private final ActionCategory category;
  private final String name;
  private final String description;
  private final Double cost;

  public PlayerAction(UUID id, ActionCategory category, String name, String description, Double cost) {
    this.id = requireId(id);
    this.category = category;
    this.name = name;
    this.description = description;
    this.cost = cost;
  }

  // #region Getters

  public UUID getId() {
    return this.id;
  }

  public ActionCategory getCategory() {
    return this.category;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public Double getCost() {
    return this.cost;
  }

  // #endregion

  private UUID requireId(UUID id) {
    return EntityValidator.ensureNotNull(CLAZZ, id, "id");
  }
}
