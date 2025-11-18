package com.udla.markenx.game.domain.model.consumer;

import java.util.List;

public class ConsumerProfile {
  private final List<ConsumerSubfactor> subfactors;

  public ConsumerProfile(List<ConsumerSubfactor> subfactors) {
    this.subfactors = subfactors;
  }

  public List<ConsumerSubfactor> getSubfactors() {
    return this.subfactors;
  }
}
