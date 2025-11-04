package com.udla.markenx.core.valueobjects;

import com.udla.markenx.core.exceptions.InvalidValueException;

public record Score(Double value) {
  public Score {
    if (value < 0 || value > 1) {
      throw new InvalidValueException("MinimumScoreToPass",
          "debe ser mayor o igual a 0 y menor o igual a 1.");
    }
  }
}
