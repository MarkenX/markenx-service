package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.adapters;

import org.springframework.stereotype.Repository;

/**
 * Deprecated adapter kept for compatibility during refactor.
 *
 * Purpose: no-op placeholder to surface migration points. Callers should use
 * StudentAssignmentRepositoryPort and StudentAssignmentRepositoryAdapter.
 */
@Repository
@Deprecated
public class TaskAssignmentRepositoryAdapter {

  public TaskAssignmentRepositoryAdapter() {
    // no-op
  }

  public Object getByTaskIdAndStudentId(Long taskId, Long studentId) {
    // During refactor, use
    // StudentAssignmentRepositoryPort#getByAssignmentIdAndStudentId instead
    return null;
  }

  public Object create(Object taskAssignment) {
    throw new UnsupportedOperationException("TaskAssignment creation is deprecated; use StudentAssignment");
  }
}
