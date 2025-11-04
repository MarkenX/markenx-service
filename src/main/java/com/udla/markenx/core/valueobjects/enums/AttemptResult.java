package com.udla.markenx.core.valueobjects.enums;

public enum AttemptResult {
  APPROVED("Aprobado"),
  DISAPPROVED("Desaprobado");

  private final String label;

  AttemptResult(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
