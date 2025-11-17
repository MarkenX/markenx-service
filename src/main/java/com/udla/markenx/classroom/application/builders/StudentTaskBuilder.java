package com.udla.markenx.classroom.application.builders;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.domain.models.StudentTask;
import com.udla.markenx.classroom.domain.models.Task;

@Component
public class StudentTaskBuilder {

  private Task task;
  private UUID studentId;
  private Long studentSerialNumber;

  public StudentTaskBuilder reset() {
    this.task = null;
    this.studentId = null;
    this.studentSerialNumber = null;
    return this;
  }

  public StudentTaskBuilder setTask(Task assignment) {
    this.task = assignment;
    return this;
  }

  public StudentTaskBuilder setStudentId(UUID studentId) {
    this.studentId = studentId;
    return this;
  }

  public StudentTaskBuilder setStudentSerialNumber(Long studentSerialNumber) {
    this.studentSerialNumber = studentSerialNumber;
    return this;
  }

  public StudentTask build() {
    return new StudentTask(task, studentId, studentSerialNumber);
  }
}
