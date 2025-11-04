package com.udla.markenx.infrastructure.out.persistance.database.utils.generators.interfaces;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

public interface RandomAssignmentDataGenerator {
  String title();

  String summary();

  AssignmentStatus status();
}
