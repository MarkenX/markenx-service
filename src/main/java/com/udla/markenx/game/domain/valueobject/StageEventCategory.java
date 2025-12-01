package com.udla.markenx.game.domain.valueobject;

public enum StageEventCategory {
  DEMOGRAPHIC("Demográfico"),
  ECONOMIC("Económico"),
  POLITICAL("Político"),
  CULTURAL("Cultural"),
  NATURE("Naturaleza"),
  TECHNOLOGICAL("Tecnológico");

  private final String label;

  StageEventCategory(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
