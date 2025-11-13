package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.mappers;

import com.udla.markenx.core.exceptions.UtilityClassInstantiationException;

/**
 * Deprecated mapper placeholder for TaskAssignment.
 *
 * Purpose: kept as a compile-time placeholder during the refactor. Use
 * StudentAssignmentMapper for mapping student-centric assignment entities.
 */
@Deprecated
public final class TaskAssignmentMapper {

  private TaskAssignmentMapper() {
    throw new UtilityClassInstantiationException(getClass());
  }

  @Deprecated
  public static void unsupported() {
    throw new UnsupportedOperationException("TaskAssignment is deprecated; use StudentAssignmentMapper instead");
  }
}
