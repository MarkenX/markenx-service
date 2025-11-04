package com.udla.markenx.infrastructure.out.data.random.generators.faker;

import com.github.javafaker.Faker;
import com.udla.markenx.application.ports.out.data.random.generators.RandomAssignmentDataGeneratorPort;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

public class FakerRandomAssignmentDataGenerator implements RandomAssignmentDataGeneratorPort {

  private final Faker faker;

  public FakerRandomAssignmentDataGenerator(Faker faker) {
    this.faker = faker;
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
}
