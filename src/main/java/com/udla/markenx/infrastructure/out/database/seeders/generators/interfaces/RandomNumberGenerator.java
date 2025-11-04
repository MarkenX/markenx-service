package com.udla.markenx.infrastructure.out.database.seeders.generators.interfaces;

public interface RandomNumberGenerator {
  int positiveInteger(int max);

  double positiveDecimal(double max);
}
