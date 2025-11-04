package com.udla.markenx.infrastructure.out.database.seeders.generators.classes.faker;

import com.github.javafaker.Faker;
import com.udla.markenx.infrastructure.out.database.seeders.generators.interfaces.RandomPersonDataGenerator;

public class FakerRandomPersonDataGenerator implements RandomPersonDataGenerator {

  private final Faker faker;

  public FakerRandomPersonDataGenerator(Faker faker) {
    this.faker = faker;
  }

  public String firstName() {
    return faker.name().firstName();
  }

  public String lastName() {
    return faker.name().lastName();
  }
}
