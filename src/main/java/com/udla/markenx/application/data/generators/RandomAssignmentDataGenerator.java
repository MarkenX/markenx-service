package com.udla.markenx.application.data.generators;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

public interface RandomAssignmentDataGenerator {
  String title();

  String summary();

  AssignmentStatus status();
}
