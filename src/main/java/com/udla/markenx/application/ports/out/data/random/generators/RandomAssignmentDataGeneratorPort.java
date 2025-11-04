package com.udla.markenx.application.ports.out.data.random.generators;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

public interface RandomAssignmentDataGeneratorPort {
  String title();

  String summary();

  AssignmentStatus status();
}
