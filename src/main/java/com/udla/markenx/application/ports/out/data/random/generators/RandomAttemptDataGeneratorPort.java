package com.udla.markenx.application.ports.out.data.random.generators;

import java.time.Duration;
import java.time.LocalDate;

public interface RandomAttemptDataGeneratorPort {
  double score();

  LocalDate date(LocalDate taskCreatedDate, LocalDate taskDueDate);

  Duration duration();
}
