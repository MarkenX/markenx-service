package com.udla.markenx.classroom.core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.udla.markenx.classroom.core.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.core.interfaces.StudentAssignment;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;
import com.udla.markenx.classroom.core.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.classroom.core.valueobjects.enums.DomainBaseModelStatus;

public class StudentTask extends StudentAssignment<Task> {
  private static final Class<StudentTask> CLAZZ = StudentTask.class;
  private static final String PREFIX = "STT";

  private final String code;
  private final List<Attempt> attempts;

  // public StudentTask(UUID id, String code, DomainBaseModelStatus status,
  // Student student, Task task,
  // List<Attempt> attempts, String createdBy, LocalDateTime createdAt,
  // LocalDateTime updatedAt) {
  public StudentTask(
      UUID id,
      String code,
      DomainBaseModelStatus status,
      Task task,
      UUID studentId,
      Long studentSerialNumber,
      List<Attempt> attempts,
      String createdBy,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    // super(id, code, status, task, student, createdBy, createdAt, updatedAt);
    super(id, code, status, task, studentId, studentSerialNumber, createdBy, createdAt, updatedAt);
    this.attempts = requireAttempts(attempts);
    this.code = requireCode(code);
  }

  // public StudentTask(Student student, Task task, String createdBy) {
  public StudentTask(
      Task task,
      UUID studentId,
      Long studentSerialNumber,
      String createdBy) {
    // super(task, student, createdBy);
    super(task, studentId, studentSerialNumber, createdBy);
    this.attempts = new ArrayList<>();
    this.code = generateCode();
  }

  // public StudentTask(Student student, Task task) {
  public StudentTask(
      Task task,
      UUID studentId,
      Long studentSerialNumber) {
    super(task, studentId, studentSerialNumber);
    this.attempts = new ArrayList<>();
    this.code = generateCode();
  }

  public String getCode() {
    return this.code;
  }

  public List<Attempt> getAttempts() {
    return List.copyOf(attempts);
  }

  public int getActiveAttempt() {
    return this.attempts.size();
  }

  public void addAttempt(Attempt attempt) {
    this.attempts.add(validateAttempt(attempt));
    updateStatus();
  }

  public void addAttempts(List<Attempt> attempts) {
    this.attempts.addAll(attempts);
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
      this.assignmentStatus = AssignmentStatus.OUTDATED;
      return;
    }
    if (isNotStarted()) {
      this.assignmentStatus = AssignmentStatus.NOT_STARTED;
      return;
    }
    if (didAnyAttemptPass()) {
      this.assignmentStatus = AssignmentStatus.COMPLETED;
      return;
    } else if (isOverMaxAttempts()) {
      this.assignmentStatus = AssignmentStatus.FAILED;
      return;
    }
    this.assignmentStatus = AssignmentStatus.IN_PROGRESS;
  }

  @Override
  protected String generateCode() {
    return String.format("%s-%s-STD%s", PREFIX, assignment.getSerialNumber(), studentSerialNumber);
  }
}
