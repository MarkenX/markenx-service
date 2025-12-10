package com.udla.markenx.game.domain.model.player;

import java.util.UUID;

import com.udla.markenx.game.domain.model.consumer.Subfactor;
import com.udla.markenx.shared.domain.utils.EntityValidator;

public class PlayerActionImpact {
  private static final Class<PlayerActionImpact> CLAZZ = PlayerActionImpact.class;

  private final UUID id;
  private final PlayerAction playerAction;
  private final Subfactor subfactor;
  private final int impact;

  public PlayerActionImpact(
      UUID id,
      PlayerAction playerAction,
      Subfactor subfactor,
      int impact) {
    this.id = requireId(id);
    this.playerAction = playerAction;
    this.subfactor = subfactor;
    this.impact = impact;
  }

  // #region Getters

  public UUID getId() {
    return this.id;
  }

  public PlayerAction getPlayerAction() {
    return this.playerAction;
  }

  public Subfactor getSubfactor() {
    return this.subfactor;
  }

  public int getImpact() {
    return this.impact;
  }

  // #endregion

  // #region Validations

  private UUID requireId(UUID id) {
    return EntityValidator.ensureNotNull(CLAZZ, id, "id");
  }

  // #endregion
}
