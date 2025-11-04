package com.udla.markenx.infrastructure.out.database.generators.interfaces;

public interface RandomNumberGenerator {
  int positiveInteger(int max);

  double positiveDecimal(double max);
}
