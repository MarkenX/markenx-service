package com.udla.markenx.classroom.infrastructure.out.data.generators.random.faker.adapters;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomStudentDataGeneratorPort;

@Component
public class FakerRandomStudentDataGenerator
    extends FakerRandomPersonDataGenerator
    implements RandomStudentDataGeneratorPort {

  private final Faker faker;

  public FakerRandomStudentDataGenerator(Faker faker) {
    super(faker);
    this.faker = faker;
  }

  public String email() {
    return faker.internet().emailAddress();
  }
}
