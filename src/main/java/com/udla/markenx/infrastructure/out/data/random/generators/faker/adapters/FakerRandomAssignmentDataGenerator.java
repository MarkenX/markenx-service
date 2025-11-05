package com.udla.markenx.infrastructure.out.data.random.generators.faker.adapters;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.application.ports.out.data.random.generators.RandomAssignmentDataGeneratorPort;
import com.udla.markenx.application.ports.out.data.random.generators.RandomDateGeneratorPort;
import com.udla.markenx.application.ports.out.data.random.generators.RandomNumberGeneratorPort;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

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
    return this.faker.lorem().paragraph();
  }

  @Override
  public AssignmentStatus status() {
    AssignmentStatus[] statuses = AssignmentStatus.values();
    return statuses[faker.random().nextInt(statuses.length)];
  }

  @Override
  public LocalDate dueDate(LocalDate from, LocalDate to) {
    return dateGenerator.dateInRange(from, to);
  }

  @Override
  public int activeAttempt(int limit) {
    return numberGenerator.positiveInteger(limit);
  }

  @Override
  public int maxAttempt(int limit) {
    return numberGenerator.positiveInteger(limit);
  }

  @Override
  public double minimumScoreToPass() {
    return numberGenerator.positiveDecimal(1);
  }
}
