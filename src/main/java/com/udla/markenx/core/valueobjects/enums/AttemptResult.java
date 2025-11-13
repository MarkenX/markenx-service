package com.udla.markenx.core.valueobjects.enums;

public enum AttemptResult {
  APPROVED("Aprobado"),
  DISAPPROVED("Reprobado"),
  UNKNOWN("Desconocido");

  private final String label;

  AttemptResult(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
