package com.udla.markenx.application.builders;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.generators.random.RandomStudentTaskDataGeneratorPort;
import com.udla.markenx.core.models.StudentTask;
import com.udla.markenx.core.models.Task;

@Component
public class StudentTaskBuilder {
  private final RandomStudentTaskDataGeneratorPort randomGenerator;

  private Long id;
  private Task task;
  private Long studentId;
  private int activeAttempt;

  public StudentTaskBuilder(RandomStudentTaskDataGeneratorPort randomGenerator) {
    this.randomGenerator = randomGenerator;
  }

  public StudentTaskBuilder reset() {
    this.id = null;
    this.task = null;
    this.studentId = null;
    this.activeAttempt = 0;
    return this;
  }

  public StudentTaskBuilder setId(Long id) {
    this.id = id;
    return this;
  }

  public StudentTaskBuilder setTask(Task assignment) {
    this.task = assignment;
    return this;
  }

  public StudentTaskBuilder setStudentId(Long studentId) {
    this.studentId = studentId;
    return this;
  }

  public StudentTaskBuilder setActiveAttempt(int activeAttempt) {
    this.activeAttempt = activeAttempt;
    return this;
  }

  public StudentTaskBuilder generateRandomActiveAttempt() {
    this.activeAttempt = randomGenerator.activeAttempt(task.getMaxAttempts());
    return this;
  }

  public StudentTask build() {
    return new StudentTask(
        id,
        task,
        studentId,
        activeAttempt);
  }
}
