package com.udla.markenx.game.domain.model.player;

import java.util.UUID;

import com.udla.markenx.game.domain.valueobject.PlayerActionCategory;
import com.udla.markenx.shared.domain.util.EntityValidator;

public class PlayerAction {
  private static final Class<PlayerAction> CLAZZ = PlayerAction.class;

  private final UUID id;
  private final PlayerActionCategory category;
  private final String name;
  private final String description;
  private final Double cost;

  public PlayerAction(
      UUID id,
      PlayerActionCategory category,
      String name,
      String description,
      Double cost) {
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

  public PlayerActionCategory getCategory() {
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

  // #region Validations

  private UUID requireId(UUID id) {
    return EntityValidator.ensureNotNull(CLAZZ, id, "id");
  }

  // #endregion
}
