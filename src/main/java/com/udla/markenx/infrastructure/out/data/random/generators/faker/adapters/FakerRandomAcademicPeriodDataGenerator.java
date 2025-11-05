package com.udla.markenx.infrastructure.out.data.random.generators.faker.adapters;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.application.ports.out.data.random.generators.RandomAcademicPeriodDataGeneratorPort;

@Component
public class FakerRandomAcademicPeriodDataGenerator implements RandomAcademicPeriodDataGeneratorPort {

  private final Faker faker;

  public FakerRandomAcademicPeriodDataGenerator(Faker faker) {
    this.faker = faker;
  }

  public String label() {
    return this.faker.animal().name();
  }
}
