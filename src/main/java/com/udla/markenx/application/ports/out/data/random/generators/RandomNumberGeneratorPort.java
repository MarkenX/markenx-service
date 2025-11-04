package com.udla.markenx.application.ports.out.data.random.generators;

public interface RandomNumberGeneratorPort {
  int positiveInteger(int max);

  double positiveDecimal(double max);
}
