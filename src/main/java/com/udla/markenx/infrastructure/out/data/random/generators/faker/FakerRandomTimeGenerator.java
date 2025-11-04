package com.udla.markenx.infrastructure.out.data.random.generators.faker;

import com.github.javafaker.Faker;
import com.udla.markenx.application.ports.out.data.random.generators.RandomTimeGeneratorPort;

import java.time.Duration;

import org.springframework.stereotype.Component;

@Component
public class FakerRandomTimeGenerator implements RandomTimeGeneratorPort {

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
