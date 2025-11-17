package com.udla.markenx.classroom.core.valueobjects.enums;

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
