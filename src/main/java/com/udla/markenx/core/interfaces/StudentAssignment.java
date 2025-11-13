package com.udla.markenx.core.interfaces;

import java.util.Objects;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

public abstract class StudentAssignment<A extends Assignment> {
  private final Long id;
  private final Long studentId;
  protected final A assignment;
  protected AssignmentStatus currentStatus;

  public StudentAssignment(Long id, A assignment, Long studentId) {
    this.id = id;
    this.assignment = assignment;
    this.studentId = studentId;
    this.currentStatus = AssignmentStatus.NOT_STARTED;
  }

  public StudentAssignment(A assignment, Long studentId) {
    this(null, assignment, studentId);
  }

  public Long getId() {
    return id;
  }

  public Long getStudentId() {
    return studentId;
  }

  public A getAssignment() {
    return assignment;
  }

  public AssignmentStatus getCurrentStatus() {
    return currentStatus;
  }

  public void setCurrentStatus(AssignmentStatus currentStatus) {
    this.currentStatus = currentStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    StudentAssignment<?> that = (StudentAssignment<?>) o;
    return Objects.equals(id, that.id)
        && Objects.equals(studentId, that.studentId)
        && Objects.equals(assignment, that.assignment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, studentId, assignment);
  }

  public abstract void updateStatus();
}
