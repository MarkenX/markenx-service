package com.udla.markenx.classroom.terms.domain.valueobjects;

import lombok.Getter;

@Getter
public enum TermStatus {
  ACTIVE(""),
  ENDED(""),
  UPCOMING("");

  private final String label;

  TermStatus(String label) {
    this.label = label;
  }
}