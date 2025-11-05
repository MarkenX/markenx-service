package com.udla.markenx.infrastructure.out.data.random.generators.faker.adapters;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.application.ports.out.data.random.generators.RandomStudentDataGeneratorPort;

@Component
public class FakerRandomStudentDataGenerator extends FakerRandomPersonDataGenerator
    implements RandomStudentDataGeneratorPort {

  public FakerRandomStudentDataGenerator(Faker faker) {
    super(faker);
  }
}
