package com.udla.markenx.classroom.academicterms.domain.valueobjects;

public enum AcademicTermStatus {
  ACTIVE(""),
  ENDED(""),
  UPCOMING("");

  private final String label;

  AcademicTermStatus(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}