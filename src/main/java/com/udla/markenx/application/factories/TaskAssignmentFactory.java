package com.udla.markenx.application.factories;

import org.springframework.stereotype.Component;

/**
 * Deprecated factory kept during refactor.
 *
 * Purpose: callers should migrate to StudentAssignmentFactory. Methods here
 * throw UnsupportedOperationException to make migration explicit.
 */
@Component
@Deprecated
public class TaskAssignmentFactory {

  public TaskAssignmentFactory() {
    // no-op
  }

  public Object create(Long taskId, Long studentId) {
    throw new UnsupportedOperationException("TaskAssignmentFactory is deprecated; use StudentAssignmentFactory");
  }
}
