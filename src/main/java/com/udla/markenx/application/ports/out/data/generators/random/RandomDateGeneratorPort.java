package com.udla.markenx.application.ports.out.data.generators.random;

import java.time.LocalDate;

public interface RandomDateGeneratorPort {
  LocalDate dateInFuture(int maxDaysInFuture);

  LocalDate dateInPast(int maxDaysInPast);

  LocalDate dateInRange(LocalDate from, LocalDate to);
}
