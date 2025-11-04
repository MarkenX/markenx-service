package com.udla.markenx.application.ports.out.data.random.generators;

import java.time.LocalDate;

public interface RandomDateGeneratorPort {
  LocalDate dateInFuture(int maxDaysInFuture);

  LocalDate dateInPast(int maxDaysInPast);
}
