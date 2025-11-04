package com.udla.markenx.application.data.generators;

public interface RandomNumberGenerator {
  int positiveInteger(int max);

  double positiveDecimal(double max);
}
