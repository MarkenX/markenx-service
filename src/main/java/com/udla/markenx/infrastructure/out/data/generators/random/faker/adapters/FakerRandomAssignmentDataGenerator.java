package com.udla.markenx.infrastructure.out.data.generators.random.faker.adapters;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.application.ports.out.data.generators.random.RandomAssignmentDataGeneratorPort;
import com.udla.markenx.application.ports.out.data.generators.random.RandomDateGeneratorPort;
import com.udla.markenx.application.ports.out.data.generators.random.RandomNumberGeneratorPort;

@Component
public class FakerRandomAssignmentDataGenerator implements RandomAssignmentDataGeneratorPort {

  private final Faker faker;
  private final RandomDateGeneratorPort dateGenerator;
  private final RandomNumberGeneratorPort numberGenerator;

  public FakerRandomAssignmentDataGenerator(
      Faker faker,
      RandomDateGeneratorPort dateGenerator,
      RandomNumberGeneratorPort numberGenerator) {
    this.faker = faker;
    this.dateGenerator = dateGenerator;
    this.numberGenerator = numberGenerator;
  }

  @Override
  public String title() {
    return this.faker.app().name();
  }

  @Override
  public String summary() {
    return this.faker.lorem().paragraph(2);
  }

  @Override
  public LocalDate dueDate(LocalDate from, LocalDate to) {
    return dateGenerator.dateInRange(from, to);
  }

  @Override
  public int maxAttempt(int limit) {
    return numberGenerator.positiveIntegerBetween(1, limit);
  }

  @Override
  public double minimumScoreToPass() {
    return numberGenerator.positiveDecimal(1);
  }
}
