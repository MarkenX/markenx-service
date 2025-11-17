package com.udla.markenx.classroom.application.ports.out.data.generators.random;

import com.udla.markenx.classroom.core.valueobjects.enums.AssignmentStatus;

public interface RandomStudentTaskDataGeneratorPort {
  int activeAttempt(int maxAttempt);

  AssignmentStatus status();
}
