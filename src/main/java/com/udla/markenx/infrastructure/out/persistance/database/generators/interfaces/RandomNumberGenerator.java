package com.udla.markenx.infrastructure.out.persistance.database.generators.interfaces;

public interface RandomNumberGenerator {
  int positiveInteger(int max);

  double positiveDecimal(double max);
}
