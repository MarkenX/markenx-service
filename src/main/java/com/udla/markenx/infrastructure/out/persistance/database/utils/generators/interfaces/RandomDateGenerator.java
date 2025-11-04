package com.udla.markenx.infrastructure.out.persistance.database.utils.generators.interfaces;

import java.time.LocalDate;

public interface RandomDateGenerator {
  LocalDate dateInFuture(int maxDaysInFuture);

  LocalDate dateInPast(int maxDaysInPast);
}
