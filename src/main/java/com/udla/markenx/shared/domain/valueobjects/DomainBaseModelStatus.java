package com.udla.markenx.shared.domain.valueobjects;

public enum DomainBaseModelStatus {
  ENABLED("Habilitado"),
  DISABLED("Deshabilitado");

  private final String label;

  DomainBaseModelStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
