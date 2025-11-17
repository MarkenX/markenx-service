package com.udla.markenx.classroom.core.interfaces;

import java.time.LocalDateTime;
import java.util.UUID;

// import com.udla.markenx.core.models.Student;
import com.udla.markenx.classroom.core.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.classroom.core.valueobjects.enums.DomainBaseModelStatus;

public abstract class StudentAssignment<A extends Assignment> extends DomainBaseModel {
  // protected final Student student;
  protected final UUID studentId;
  protected final Long studentSerialNumber;
  protected final A assignment;
  protected AssignmentStatus assignmentStatus;

  // public StudentAssignment(UUID id, String code, DomainBaseModelStatus status,
  // A assignment,
  // Student student, String createdBy, LocalDateTime createdAt, LocalDateTime
  // updatedAt) {
  public StudentAssignment(
      UUID id,
      String code,
      DomainBaseModelStatus status,
      A assignment,
      UUID studentId,
      Long studentSerialNumber,
      String createdBy,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super(id, code, status, createdBy, createdAt, updatedAt);
    this.assignment = assignment;
    this.studentId = studentId;
    this.studentSerialNumber = studentSerialNumber;
    // this.student = student;
    this.assignmentStatus = AssignmentStatus.NOT_STARTED;
  }

  // public StudentAssignment(A assignment, Student student, String createdBy) {
  public StudentAssignment(
      A assignment,
      UUID studentId,
      Long studentSerialNumber,
      String createdBy) {
    super(createdBy);
    this.assignment = assignment;
    this.studentId = studentId;
    this.studentSerialNumber = studentSerialNumber;
    // this.student = student;
    this.assignmentStatus = AssignmentStatus.NOT_STARTED;
  }

  // public StudentAssignment(A assignment, Student student) {
  public StudentAssignment(
      A assignment,
      UUID studentId,
      Long studentSerialNumber) {
    super();
    this.assignment = assignment;
    this.studentId = studentId;
    this.studentSerialNumber = studentSerialNumber;
    // this.student = student;
    this.assignmentStatus = AssignmentStatus.NOT_STARTED;
  }

  // public Student getStudent() {
  // return student;
  // }

  public UUID getStudentId() {
    return this.studentId;
  }

  public A getAssignment() {
    return assignment;
  }

  public AssignmentStatus getAssignmentStatus() {
    return assignmentStatus;
  }

  public void setAssignmentStatus(AssignmentStatus currentStatus) {
    this.assignmentStatus = currentStatus;
  }

  public abstract void updateStatus();
}
