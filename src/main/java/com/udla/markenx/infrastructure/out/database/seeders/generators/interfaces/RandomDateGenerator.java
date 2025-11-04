package com.udla.markenx.infrastructure.out.database.seeders.generators.interfaces;

import java.time.LocalDate;

public interface RandomDateGenerator {
  LocalDate dateInFuture(int maxDaysInFuture);

  LocalDate dateInPast(int maxDaysInPast);
}
