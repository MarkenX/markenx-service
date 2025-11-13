package com.udla.markenx.infrastructure.out.data.generators.random.faker.adapters;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.application.ports.out.data.generators.random.RandomStudentDataGeneratorPort;

@Component
public class FakerRandomStudentDataGenerator
    extends FakerRandomPersonDataGenerator
    implements RandomStudentDataGeneratorPort {

  public FakerRandomStudentDataGenerator(Faker faker) {
    super(faker);
  }
}
