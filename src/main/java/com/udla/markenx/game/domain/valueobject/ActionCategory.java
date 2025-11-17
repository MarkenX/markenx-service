package com.udla.markenx.game.domain.valueobject;

public enum ActionCategory {
  PRODUCTION("Producción"),
  DESIGN("Diseño"),
  PRICE("Precio"),
  EXPLORATION("Exploración"),
  PUBLICITY("Publicidad");

  private final String label;

  ActionCategory(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
