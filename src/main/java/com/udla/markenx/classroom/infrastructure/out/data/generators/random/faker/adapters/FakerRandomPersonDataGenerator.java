package com.udla.markenx.classroom.infrastructure.out.data.generators.random.faker.adapters;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomPersonDataGeneratorPort;

@Component
public class FakerRandomPersonDataGenerator implements RandomPersonDataGeneratorPort {

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
