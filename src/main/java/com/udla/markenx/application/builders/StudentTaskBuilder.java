package com.udla.markenx.application.builders;

import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Student;
import com.udla.markenx.core.models.StudentTask;
import com.udla.markenx.core.models.Task;

@Component
public class StudentTaskBuilder {

  private Task task;
  private Student student;

  public StudentTaskBuilder reset() {
    this.task = null;
    this.student = null;
    return this;
  }

  public StudentTaskBuilder setTask(Task assignment) {
    this.task = assignment;
    return this;
  }

  public StudentTaskBuilder setStudent(Student student) {
    this.student = student;
    return this;
  }

  public StudentTask build() {
    return new StudentTask(student, task);
  }
}
