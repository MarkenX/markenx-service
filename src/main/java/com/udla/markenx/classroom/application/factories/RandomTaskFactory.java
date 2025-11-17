package com.udla.markenx.classroom.application.factories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.domain.models.Task;

import com.udla.markenx.classroom.application.builders.TaskBuilder;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomNumberGeneratorPort;

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

  public Task createRandomTask(UUID courseId, int academicTermYear, LocalDate limitDueDate) {
    int maxAttemptCount = numberGenerator.positiveIntegerBetween(1, MAX_ATTEMPTS);
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    Task task = taskBuilder
        .reset()
        .setCourseId(courseId)
        .setAcademicTermYear(academicTermYear)
        .randomTitle()
        .randomSummary()
        .randomDueDate(tomorrow, limitDueDate)
        .randomMaxAttempts(maxAttemptCount)
        .randomMinScoreToPass()
        .build();
    return task;
  }

  public List<Task> createRandomTasks(UUID courseId, int academicTermYear, int count, LocalDate limitDueDate) {
    List<Task> tasks = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      tasks.add(createRandomTask(courseId, academicTermYear, limitDueDate));
    }
    return tasks;
  }

  public List<Task> createRandomTasksUpTo(UUID courseId, int academicTermYear, int maxTasks, LocalDate endDate) {
    if (maxTasks <= 0) {
      throw new IllegalArgumentException("El límite debe ser mayor que cero");
    }

    int count = numberGenerator.positiveIntegerBetween(1, maxTasks);
    return createRandomTasks(courseId, academicTermYear, count, endDate);
  }
}
