package com.udla.markenx.core.models;

import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.interfaces.StudentAssignment;
import com.udla.markenx.core.utils.validators.EntityValidator;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

public class StudentTask extends StudentAssignment<Task> {
  private static final Class<StudentTask> CLAZZ = StudentTask.class;

  private final Long id;
  private final List<Attempt> attempts;

  public StudentTask(Long id, Task task, Long studentId, List<Attempt> attempts) {
    super(task, studentId);
    this.id = id;
    this.attempts = requireAttempts(attempts);
  }

  public StudentTask() {
    this(null, null, null, new ArrayList<>());
  }

  public Long getId() {
    return this.id;
  }

  public int getActiveAttempt() {
    return this.attempts.size();
  }

  public void addAttempt(Attempt attempt) {
    this.attempts.add(validateAttempt(attempt));
    updateStatus();
  }

  private boolean isNotStarted() {
    return getActiveAttempt() == 0;
  }

  private boolean didAnyAttemptPass() {
    if (attempts == null || attempts.isEmpty()) {
      return false;
    }
    return attempts.stream().anyMatch(attempt -> attempt.isApproved());
  }

  private boolean isOverMaxAttempts() {
    if (attempts == null || assignment == null) {
      return false;
    }
    return attempts.size() >= assignment.getMaxAttempts();
  }

  private Attempt validateAttempt(Attempt attempt) {
    if (isOverMaxAttempts()) {
      throw new InvalidEntityException(CLAZZ, "Máximo de intentos alcanzado.");
    }
    return EntityValidator.ensureNotNull(CLAZZ, attempt, "attempt");
  }

  private List<Attempt> requireAttempts(List<Attempt> attempts) {
    return EntityValidator.ensureNotNull(CLAZZ, attempts, "attempts");
  }

  @Override
  public void updateStatus() {
    if (assignment.isOverdue()) {
      this.currentStatus = AssignmentStatus.OUTDATED;
      return;
    }
    if (isNotStarted()) {
      this.currentStatus = AssignmentStatus.NOT_STARTED;
      return;
    }
    if (didAnyAttemptPass()) {
      this.currentStatus = AssignmentStatus.COMPLETED;
      return;
    } else if (isOverMaxAttempts()) {
      this.currentStatus = AssignmentStatus.FAILED;
      return;
    }
    this.currentStatus = AssignmentStatus.IN_PROGRESS;
  }

}
