package com.udla.markenx.application.data.random.builders;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.random.generators.RandomAssignmentDataGeneratorPort;
import com.udla.markenx.application.ports.out.data.random.generators.RandomDateGeneratorPort;
import com.udla.markenx.application.ports.out.data.random.generators.RandomNumberGeneratorPort;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

@Component
public class RandomTaskFactory {

  private static final int DEFAULT_DAYS_IN_FUTURE = 30;
  private static final int MAX_ATTEMPTS_RANGE = 10;

  private final RandomAssignmentDataGeneratorPort assignmentGenerator;
  private final RandomDateGeneratorPort dateGenerator;
  private final RandomNumberGeneratorPort numberGenerator;

  public RandomTaskFactory(
      RandomAssignmentDataGeneratorPort assignmentGenerator,
      RandomDateGeneratorPort dateGenerator,
      RandomNumberGeneratorPort numberGenerator) {
    this.assignmentGenerator = assignmentGenerator;
    this.dateGenerator = dateGenerator;
    this.numberGenerator = numberGenerator;
  }

  public TaskJpaEntity createCompletedTask(StudentJpaEntity student) {
    int maxAttempt = numberGenerator.positiveInteger(MAX_ATTEMPTS_RANGE);

    TaskJpaEntity task = new TaskJpaEntity();
    task.setTitle(assignmentGenerator.title());
    task.setSummary(assignmentGenerator.summary());
    task.setDueDate(dateGenerator.dateInFuture(DEFAULT_DAYS_IN_FUTURE));
    task.setCurrentStatus(AssignmentStatus.COMPLETED);
    task.setActiveAttempt(numberGenerator.positiveInteger(maxAttempt));
    task.setMaxAttempts(maxAttempt);
    task.setStudent(student);

    return task;
  }

  public TaskJpaEntity createInProgressTask(StudentJpaEntity student) {
    int maxAttempt = numberGenerator.positiveInteger(MAX_ATTEMPTS_RANGE);

    TaskJpaEntity task = new TaskJpaEntity();
    task.setTitle(assignmentGenerator.title());
    task.setSummary(assignmentGenerator.summary());
    task.setDueDate(dateGenerator.dateInFuture(DEFAULT_DAYS_IN_FUTURE));
    task.setCurrentStatus(AssignmentStatus.IN_PROGRESS);
    task.setActiveAttempt(numberGenerator.positiveInteger(maxAttempt));
    task.setMaxAttempts(maxAttempt);
    task.setStudent(student);

    return task;
  }
}
