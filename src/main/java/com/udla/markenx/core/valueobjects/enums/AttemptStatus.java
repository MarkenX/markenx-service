package com.udla.markenx.core.valueobjects.enums;

public enum AttemptStatus {
  APPROVED("Aprobado"),
  DISAPPROVED("Desaprobado");

  private final String label;

  AttemptStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
