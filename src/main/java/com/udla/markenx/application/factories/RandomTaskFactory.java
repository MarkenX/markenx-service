package com.udla.markenx.application.factories;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.builders.AttemptBuilder;
import com.udla.markenx.application.builders.TaskBuilder;
import com.udla.markenx.application.ports.out.data.random.generators.RandomNumberGeneratorPort;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.core.models.Task;

@Component
public class RandomTaskFactory {
  private static final int MAX_ATTEMPTS = 10;

  private final TaskBuilder taskBuilder;
  private final AttemptBuilder attemptBuilder;
  private final RandomNumberGeneratorPort numberGenerator;

  public RandomTaskFactory(
      TaskBuilder taskBuilder,
      AttemptBuilder attemptBuilder,
      RandomNumberGeneratorPort numberGenerator) {
    this.taskBuilder = taskBuilder;
    this.attemptBuilder = attemptBuilder;
    this.numberGenerator = numberGenerator;
  }

  public Task createRandomTask(LocalDate limitDueDate) {
    int attemptCount = numberGenerator.positiveInteger(MAX_ATTEMPTS);
    LocalDate today = LocalDate.now();
    Task task = taskBuilder
        .reset()
        .randomTitle()
        .randomSummary()
        .randomDueDate(today, limitDueDate)
        .randomMaxAttempts(attemptCount)
        .randomMinimumScoreToPass()
        .build();
    return task;
  }

  public Task createRandomTaskWithAttempts(LocalDate limitDueDate) {
    int attemptCount = numberGenerator.positiveInteger(MAX_ATTEMPTS);
    LocalDate today = LocalDate.now();
    Task task = taskBuilder
        .reset()
        .randomTitle()
        .randomSummary()
        .randomDueDate(today, limitDueDate)
        .randomMaxAttempts(attemptCount)
        .randomActiveAttempt()
        .randomMinimumScoreToPass()
        .build();

    for (int i = 0; i < attemptCount; i++) {
      Attempt attempt = attemptBuilder
          .reset()
          .setMinimumScoreToPass(task.getMinimumScoreToPass())
          .randomDate(task.getCreatedDate(), task.getDueDate())
          .randomScore()
          .randomDuration()
          .build();
      task.addAttempt(attempt);
    }

    return task;
  }
}
