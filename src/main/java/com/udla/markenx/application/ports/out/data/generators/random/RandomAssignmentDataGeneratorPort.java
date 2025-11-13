package com.udla.markenx.application.ports.out.data.generators.random;

import java.time.LocalDate;

public interface RandomAssignmentDataGeneratorPort {
  String title();

  String summary();

  LocalDate dueDate(LocalDate from, LocalDate to);

  int maxAttempt(int limit);

  double minimumScoreToPass();
}
