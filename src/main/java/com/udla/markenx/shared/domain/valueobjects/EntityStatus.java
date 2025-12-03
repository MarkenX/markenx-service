package com.udla.markenx.shared.domain.valueobjects;

public enum EntityStatus {
  ENABLED("Habilitado"),
  DISABLED("Deshabilitado");

  private final String label;

  EntityStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
