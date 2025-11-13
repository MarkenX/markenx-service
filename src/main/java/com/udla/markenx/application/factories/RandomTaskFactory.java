package com.udla.markenx.application.factories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Task;

import com.udla.markenx.application.builders.TaskBuilder;
import com.udla.markenx.application.ports.out.data.generators.random.RandomNumberGeneratorPort;

@Component
public class RandomTaskFactory {
  private static final int MAX_ATTEMPTS = 10;

  private final TaskBuilder taskBuilder;
  private final RandomNumberGeneratorPort numberGenerator;

  public RandomTaskFactory(
      TaskBuilder taskBuilder,
      RandomNumberGeneratorPort numberGenerator) {
    this.taskBuilder = taskBuilder;
    this.numberGenerator = numberGenerator;
  }

  public Task createRandomTask(LocalDate limitDueDate) {
    int maxAttemptCount = numberGenerator.positiveIntegerBetween(1, MAX_ATTEMPTS);
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    Task task = taskBuilder
        .reset()
        .generateId()
        .generateRandomTitle()
        .generateRandomSummary()
        .generateRandomDueDate(tomorrow, limitDueDate)
        .generateRandomMaxAttempts(maxAttemptCount)
        .generateRandomMinimumScoreToPass()
        .build();
    return task;
  }

  public List<Task> createRandomTasks(int count, LocalDate limitDueDate) {
    List<Task> tasks = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      tasks.add(createRandomTask(limitDueDate));
    }
    return tasks;
  }

  public List<Task> createRandomTasksUpTo(int maxTasks, LocalDate endDate) {
    if (maxTasks <= 0) {
      throw new IllegalArgumentException("El límite debe ser mayor que cero");
    }

    int count = numberGenerator.positiveIntegerBetween(1, maxTasks);
    return createRandomTasks(count, endDate);
  }
}
