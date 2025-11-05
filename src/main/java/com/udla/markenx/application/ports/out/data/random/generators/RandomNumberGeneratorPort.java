package com.udla.markenx.application.ports.out.data.random.generators;

public interface RandomNumberGeneratorPort {
  int positiveIntegerBetween(int min, int max);

  int positiveInteger(int max);

  double positiveDecimal(double max);
}
