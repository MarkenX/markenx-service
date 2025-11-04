package com.udla.markenx.infrastructure.out.persistance.database.utils.generators.interfaces;

public interface RandomNumberGenerator {
  int positiveInteger(int max);

  double positiveDecimal(double max);
}
