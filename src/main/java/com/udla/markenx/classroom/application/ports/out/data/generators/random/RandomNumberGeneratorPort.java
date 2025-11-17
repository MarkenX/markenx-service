package com.udla.markenx.classroom.application.ports.out.data.generators.random;

public interface RandomNumberGeneratorPort {
  int positiveIntegerBetween(int min, int max);

  int positiveInteger(int max);

  double positiveDecimal(double max);
}
