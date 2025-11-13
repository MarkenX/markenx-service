package com.udla.markenx.application.builders;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Task;
import com.udla.markenx.application.ports.out.data.generators.EntityIdGenerator;
import com.udla.markenx.application.ports.out.data.generators.random.RandomAssignmentDataGeneratorPort;

@Component
public class TaskBuilder {

  private final RandomAssignmentDataGeneratorPort randomGenerator;
  private final EntityIdGenerator idGenerator;

  private Long id;
  private String title;
  private String summary;
  private LocalDate dueDate;
  private int activeAttempt;
  private int maxAttempt;
  private double minimumScoreToPass;

  public TaskBuilder(
      RandomAssignmentDataGeneratorPort randomGenerator,
      EntityIdGenerator idGenerator) {
    this.randomGenerator = randomGenerator;
    this.idGenerator = idGenerator;
  }

  public TaskBuilder reset() {
    this.id = null;
    this.title = null;
    this.summary = null;
    this.dueDate = null;
    this.activeAttempt = 0;
    this.maxAttempt = 0;
    this.minimumScoreToPass = 0.0;
    this.id = null;
    return this;
  }

  public TaskBuilder setId(Long id) {
    this.id = id;
    return this;
  }

  public TaskBuilder generateId() {
    this.id = idGenerator.nextId();
    return this;
  }

  public TaskBuilder generateRandomTitle() {
    this.title = randomGenerator.title();
    return this;
  }

  public TaskBuilder generateRandomSummary() {
    this.summary = randomGenerator.summary();
    return this;
  }

  public TaskBuilder generateRandomDueDate(LocalDate start, LocalDate end) {
    this.dueDate = randomGenerator.dueDate(start, end);
    return this;
  }

  public TaskBuilder generateRandomMaxAttempts(int limit) {
    this.maxAttempt = randomGenerator.maxAttempt(limit);
    return this;
  }

  public TaskBuilder generateRandomMinimumScoreToPass() {
    this.minimumScoreToPass = randomGenerator.minimumScoreToPass();
    return this;
  }

  public Task build() {
    return new Task(
        id,
        title,
        summary,
        dueDate,
        maxAttempt,
        activeAttempt,
        minimumScoreToPass);
  }
}
