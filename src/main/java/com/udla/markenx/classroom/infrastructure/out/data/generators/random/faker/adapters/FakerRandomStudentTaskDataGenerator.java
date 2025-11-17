package com.udla.markenx.classroom.infrastructure.out.data.generators.random.faker.adapters;

import com.github.javafaker.Faker;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomStudentTaskDataGeneratorPort;
import com.udla.markenx.classroom.domain.valueobjects.enums.AssignmentStatus;

public class FakerRandomStudentTaskDataGenerator implements RandomStudentTaskDataGeneratorPort {

  private final Faker faker;
  private final RandomNumberGeneratorPort numberGenerator;

  public FakerRandomStudentTaskDataGenerator(Faker faker, RandomNumberGeneratorPort numberGenerator) {
    this.faker = faker;
    this.numberGenerator = numberGenerator;
  }

  @Override
  public AssignmentStatus status() {
    AssignmentStatus[] statuses = AssignmentStatus.values();
    return statuses[faker.random().nextInt(statuses.length)];
  }

  @Override
  public int activeAttempt(int limit) {
    return numberGenerator.positiveInteger(limit);
  }
}
