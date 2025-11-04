package com.udla.markenx.application.builders;

import java.time.Duration;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.random.generators.RandomAttemptDataGeneratorPort;
import com.udla.markenx.core.models.Attempt;

@Component
public class AttemptBuilder {

  private final RandomAttemptDataGeneratorPort randomGenerator;

  private double score;
  private LocalDate date;
  private Duration duration;
  private double minimumScoreToPass;

  public AttemptBuilder reset() {
    this.score = 0.0;
    this.date = null;
    this.duration = null;
    this.minimumScoreToPass = 0.0;
    return this;
  }

  public AttemptBuilder(RandomAttemptDataGeneratorPort randomGenerator) {
    this.randomGenerator = randomGenerator;
  }

  public AttemptBuilder setMinimumScoreToPass(double value) {
    this.minimumScoreToPass = value;
    return this;
  }

  public AttemptBuilder randomScore() {
    this.score = randomGenerator.score();
    return this;
  }

  public AttemptBuilder randomDate(LocalDate taskCreatedDate, LocalDate taskDueDate) {
    this.date = randomGenerator.date(taskCreatedDate, taskDueDate);
    return this;
  }

  public AttemptBuilder randomDuration() {
    this.duration = randomGenerator.duration();
    return this;
  }

  public Attempt build() {
    return new Attempt(score, date, duration, minimumScoreToPass);
  }
}
