package com.udla.markenx.application.ports.out.data.generators.random;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

public interface RandomStudentTaskDataGeneratorPort {
  int activeAttempt(int maxAttempt);

  AssignmentStatus status();
}
