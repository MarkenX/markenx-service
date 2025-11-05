package com.udla.markenx.application.builders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Task;
import com.udla.markenx.application.ports.out.data.random.generators.RandomAssignmentDataGeneratorPort;

@Component
public class TaskBuilder {

  private final RandomAssignmentDataGeneratorPort randomGenerator;

  private String title;
  private String summary;
  private LocalDate dueDate;
  private int activeAttempt;
  private int maxAttempt;
  private double minimumScoreToPass;

  public TaskBuilder(RandomAssignmentDataGeneratorPort randomGenerator) {
    this.randomGenerator = randomGenerator;
  }

  public TaskBuilder reset() {
    this.title = null;
    this.summary = null;
    this.dueDate = null;
    this.activeAttempt = 0;
    this.maxAttempt = 0;
    this.minimumScoreToPass = 0.0;
    return this;
  }

  public TaskBuilder randomTitle() {
    this.title = randomGenerator.title();
    return this;
  }

  public TaskBuilder randomSummary() {
    this.summary = randomGenerator.summary();
    return this;
  }

  public TaskBuilder randomDueDate(LocalDate start, LocalDate end) {
    this.dueDate = randomGenerator.dueDate(start, end);
    return this;
  }

  public TaskBuilder randomMaxAttempts(int limit) {
    this.maxAttempt = randomGenerator.maxAttempt(limit);
    return this;
  }

  public TaskBuilder randomActiveAttempt() {
    this.activeAttempt = randomGenerator.activeAttempt(this.maxAttempt);
    return this;
  }

  public TaskBuilder randomMinimumScoreToPass() {
    this.minimumScoreToPass = randomGenerator.minimumScoreToPass();
    return this;
  }

  public Task build() {
    LocalDateTime now = LocalDateTime.now();
    ;
    return new Task(title, summary, dueDate, maxAttempt, activeAttempt, minimumScoreToPass, now, now);
  }
}
