package com.udla.markenx.infrastructure.out.persistance.database.utils.generators.faker;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.application.data.generators.RandomPersonDataGenerator;

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
