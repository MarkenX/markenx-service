package com.udla.markenx.classroom.infrastructure.out.data.generators.random.faker.adapters;

import java.time.Duration;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomAttemptDataGeneratorPort;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomDateGeneratorPort;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomTimeGeneratorPort;

@Component
public class FakerRandomAttemptDataGenerator implements RandomAttemptDataGeneratorPort {

  private final RandomNumberGeneratorPort numberGenerator;
  private final RandomDateGeneratorPort dateGenerator;
  private final RandomTimeGeneratorPort timeGenerator;

  public FakerRandomAttemptDataGenerator(
      RandomNumberGeneratorPort numberGenerator,
      RandomDateGeneratorPort dateGenerator,
      RandomTimeGeneratorPort timeGenerator) {
    this.numberGenerator = numberGenerator;
    this.dateGenerator = dateGenerator;
    this.timeGenerator = timeGenerator;
  }

  @Override
  public double score() {
    return numberGenerator.positiveDecimal(1);
  }

  @Override
  public LocalDate date(LocalDate taskCreatedDate, LocalDate taskDueDate) {
    return dateGenerator.dateInRange(taskCreatedDate, taskDueDate);
  }

  @Override
  public Duration duration() {
    return timeGenerator.durationInMinutes(60);
  }
}
