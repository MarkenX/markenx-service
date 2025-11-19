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
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plusDays(1);

    // Due dates must be in the future for domain validation
    // Use the smaller of: term end date OR 6 months from now
    LocalDate maxDate = limitDueDate.isAfter(today) ? limitDueDate : today.plusMonths(6);

    // Ensure we have at least a 7-day window for random dates
    if (maxDate.isBefore(tomorrow.plusDays(7))) {
      maxDate = tomorrow.plusDays(30);
    }

    Task task = taskBuilder
        .reset()
        .setCourseId(courseId)
        .setAcademicTermYear(academicTermYear)
        .randomTitle()
        .randomSummary()
        .randomDueDate(tomorrow, maxDate)
        .randomMaxAttempts(maxAttemptCount)
        .randomMinScoreToPass()
        .build();
    return task;
  }

  /**
   * Crea una tarea con fecha histórica (permite fechas en el pasado).
   * Útil para seeders que simulan datos históricos.
   * 
   * @param termStartDate Fecha de inicio del período académico
   * @param termEndDate   Fecha de fin del período académico
   */
  public Task createHistoricalTask(UUID courseId, int academicTermYear,
      LocalDate termStartDate, LocalDate termEndDate) {
    int maxAttemptCount = numberGenerator.positiveIntegerBetween(1, MAX_ATTEMPTS);

    // Calcular días totales del período
    int totalDays = (int) termStartDate.until(termEndDate).getDays();

    // Asegurar que haya al menos 2 días de diferencia
    if (totalDays < 2) {
      totalDays = 2;
    }

    // Asegurar un rango válido: entre 1 día y el total de días del período
    int minDays = 1;
    int maxDays = Math.max(totalDays - 1, 2);

    // Generar fecha de vencimiento dentro del rango del período académico
    LocalDate dueDate = termStartDate.plusDays(
        numberGenerator.positiveIntegerBetween(minDays, maxDays));

    Task task = taskBuilder
        .reset()
        .setCourseId(courseId)
        .setAcademicTermYear(academicTermYear)
        .randomTitle()
        .randomSummary()
        .setDueDate(dueDate)
        .randomMaxAttempts(maxAttemptCount)
        .randomMinScoreToPass()
        .setCreatedBy("SEEDER")
        .allowPastDates()
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

  /**
   * Crea tareas históricas (permite fechas en el pasado).
   */
  public List<Task> createHistoricalTasks(UUID courseId, int academicTermYear, int count,
      LocalDate termStartDate, LocalDate termEndDate) {
    List<Task> tasks = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      tasks.add(createHistoricalTask(courseId, academicTermYear, termStartDate, termEndDate));
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
