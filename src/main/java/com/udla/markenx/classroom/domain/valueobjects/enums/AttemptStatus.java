package com.udla.markenx.classroom.domain.valueobjects.enums;

public enum AttemptStatus {
  COMPLETED("Completado"),
  INTERRUPTED("Interrumpido");

  private final String label;

  AttemptStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
