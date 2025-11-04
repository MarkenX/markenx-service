package com.udla.markenx.application.factories;

import java.time.LocalDate;

import com.udla.markenx.application.builders.AttemptBuilder;
import com.udla.markenx.application.builders.TaskBuilder;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.core.models.Task;

public class RandomTaskFactory {
  private final TaskBuilder taskBuilder;
  private final AttemptBuilder attemptBuilder;

  public RandomTaskFactory(TaskBuilder taskBuilder, AttemptBuilder attemptBuilder) {
    this.taskBuilder = taskBuilder;
    this.attemptBuilder = attemptBuilder;
  }

  public Task createRandomTaskWithAttempts(int attemptCount, LocalDate limitDueDate) {
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
