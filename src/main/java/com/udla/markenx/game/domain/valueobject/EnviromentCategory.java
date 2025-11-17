package com.udla.markenx.game.domain.valueobject;

public enum EnviromentCategory {
  DEMOGRAPHIC("Demográfico"),
  ECONOMIC("Económico"),
  POLITICAL("Político"),
  CULTURAL("Cultural"),
  NATURE("Naturaleza"),
  TECHNOLOGICAL("Tecnológico");

  private final String label;

  EnviromentCategory(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
