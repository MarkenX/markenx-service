package com.udla.markenx.infrastructure.out.data.random.generators.faker.adapters;

import java.time.Duration;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.random.generators.RandomAttemptDataGeneratorPort;
import com.udla.markenx.application.ports.out.data.random.generators.RandomDateGeneratorPort;
import com.udla.markenx.application.ports.out.data.random.generators.RandomNumberGeneratorPort;
import com.udla.markenx.application.ports.out.data.random.generators.RandomTimeGeneratorPort;

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
