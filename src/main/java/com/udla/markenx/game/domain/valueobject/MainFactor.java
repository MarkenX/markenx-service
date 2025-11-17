package com.udla.markenx.game.domain.valueobject;

public enum MainFactor {
  PSYCHOLOGICAL("Psicológico", "Descripción del factor psicológico"),
  PERSONAL("Personal", "Descripción del factor personal"),
  SOCIAL("Social", "Descripción del factor social"),
  CULTURAL("Cultural", "Descripción del factor cultural");

  private final String label;
  private final String description;

  MainFactor(String label, String description) {
    this.label = label;
    this.description = description;
  }

  public String getLabel() {
    return label;
  }

  public String getDescription() {
    return this.description;
  }
}
