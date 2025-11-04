package com.udla.markenx.application.ports.out.data.random.generators;

import java.time.LocalDate;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

public interface RandomAssignmentDataGeneratorPort {
  String title();

  String summary();

  AssignmentStatus status();

  LocalDate dueDate(LocalDate from, LocalDate to);

  int activeAttempt(int maxAttempt);

  int maxAttempt(int limit);

  double minimumScoreToPass();
}
