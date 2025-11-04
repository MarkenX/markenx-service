package com.udla.markenx.infrastructure.out.persistance.database.utils.generators.faker;

import com.github.javafaker.Faker;
import com.udla.markenx.application.data.generators.RandomAssignmentDataGenerator;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

public class FakerRandomAssignmentDataGenerator implements RandomAssignmentDataGenerator {

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
