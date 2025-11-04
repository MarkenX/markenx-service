package com.udla.markenx.application.data.generators;

import java.time.LocalDate;

public interface RandomDateGenerator {
  LocalDate dateInFuture(int maxDaysInFuture);

  LocalDate dateInPast(int maxDaysInPast);
}
