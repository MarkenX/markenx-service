package com.udla.markenx.infrastructure.out.database.seeders.generators.classes.faker;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.infrastructure.out.database.seeders.generators.interfaces.RandomPersonDataGenerator;

@Component
public class FakerRandomPersonDataGenerator implements RandomPersonDataGenerator {

  private final Faker faker;

  public FakerRandomPersonDataGenerator(Faker faker) {
    this.faker = faker;
  }

  @Override
  public String firstName() {
    return faker.name().firstName();
  }

  @Override
  public String lastName() {
    return faker.name().lastName();
  }
}
