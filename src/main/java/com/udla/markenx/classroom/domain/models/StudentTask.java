package com.udla.markenx.classroom.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.udla.markenx.shared.domain.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.domain.interfaces.StudentAssignment;
import com.udla.markenx.classroom.domain.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.shared.domain.utils.EntityValidator;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

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
      EntityStatus status,
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
    this.code = code;
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
    // Si ya se alcanzó el score mínimo para aprobar, entonces es COMPLETED
    if (didAnyAttemptPass()) {
      this.assignmentStatus = AssignmentStatus.COMPLETED;
      return;
    }

    // Si la tarea ya pasó (vencida)
    if (assignment.isOverdue()) {
      // Si tiene intentos pero ninguno aprobado, entonces FAILED
      if (!isNotStarted()) {
        this.assignmentStatus = AssignmentStatus.FAILED;
      } else {
        // Si no tiene intentos y ya venció, entonces OUTDATED
        this.assignmentStatus = AssignmentStatus.OUTDATED;
      }
      return;
    }

    // Si la tarea está en el futuro o presente
    if (isNotStarted()) {
      this.assignmentStatus = AssignmentStatus.NOT_STARTED;
      return;
    }

    // Si tiene intentos pero no ha aprobado y no ha vencido
    this.assignmentStatus = AssignmentStatus.IN_PROGRESS;
  }

  @Override
  protected String generateCode() {
    if (assignment == null || assignment.getSerialNumber() == null || studentSerialNumber == null) {
      return null; // Code will be generated after persistence
    }
    return String.format("%s-%s-STD%s", PREFIX, assignment.getSerialNumber(), studentSerialNumber);
  }
}
