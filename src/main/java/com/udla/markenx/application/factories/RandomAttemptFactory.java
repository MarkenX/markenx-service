package com.udla.markenx.application.factories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.builders.AttemptBuilder;
import com.udla.markenx.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.core.models.StudentTask;

@Component
public class RandomAttemptFactory {

  private final AttemptBuilder attemptBuilder;
  private final RandomNumberGeneratorPort numberGenerator;

  public RandomAttemptFactory(
      AttemptBuilder attemptBuilder,
      RandomNumberGeneratorPort numberGenerator) {
    this.attemptBuilder = attemptBuilder;
    this.numberGenerator = numberGenerator;
  }

  public Attempt createRandomAttempt(StudentTask studentTask, double minScoreToPass) {
    Attempt attempt = attemptBuilder
        .reset()
        .setStudentSequence(studentTask.getStudentSequence())
        .setTaskSequence(studentTask.getTaskSequence())
        .setTaskMinSocreToPass(minScoreToPass)
        .randomScore()
        .randomDuration()
        .build();
    return attempt;
  }

  public List<Attempt> createRandomAttempts(StudentTask studentTask, double minScoreToPass, int count) {
    List<Attempt> attempts = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      attempts.add(createRandomAttempt(studentTask, minScoreToPass));
    }
    return attempts;
  }

  public List<Attempt> createRandomAttemptsUpTo(StudentTask studentTask, double minScoreToPass, int maxAttempts) {
    if (maxAttempts <= 0) {
      throw new IllegalArgumentException("El límite debe ser mayor que cero");
    }

    int count = numberGenerator.positiveIntegerBetween(1, maxAttempts);
    return createRandomAttempts(studentTask, minScoreToPass, count);
  }
}
