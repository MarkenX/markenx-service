package com.udla.markenx.infrastructure.out.database.seeders.generators.classes.faker;

import com.github.javafaker.Faker;

import java.time.Duration;

import org.springframework.stereotype.Component;

import com.udla.markenx.infrastructure.out.database.seeders.generators.interfaces.RandomTimeGenerator;

@Component
public class FakerRandomTimeGenerator implements RandomTimeGenerator {

  private final Faker faker;

  public FakerRandomTimeGenerator(Faker faker) {
    this.faker = faker;
  }

  @Override
  public Duration durationInMinutes(int maxMinutes) {
    long minutes = faker.number().numberBetween(1, maxMinutes);
    return Duration.ofMinutes(minutes);
  }
}
