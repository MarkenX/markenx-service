package com.udla.markenx.game.domain.valueobject;

public enum MainFactor {
  PSYCHOLOGICAL("Psicológico"),
  PERSONAL("Personal"),
  SOCIAL("Social"),
  CULTURAL("Cultural");

  private final String label;

  MainFactor(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
