package com.udla.markenx.infrastructure.out.data.random.generators.faker.adapters;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.application.ports.out.data.random.generators.RandomNumberGeneratorPort;

@Component
public class FakerRandomNumberGenerator implements RandomNumberGeneratorPort {

  private static final int MIN_POSITIVE_NUMBER = 1;

  private final Faker faker;

  public FakerRandomNumberGenerator(Faker faker) {
    this.faker = faker;
  }

  @Override
  public int positiveInteger(int limit) {
    if (limit <= MIN_POSITIVE_NUMBER) {
      throw new IllegalArgumentException(
          "El límite debe ser mayor que " + MIN_POSITIVE_NUMBER + " (valor recibido: " + limit + ")");
    }
    return faker.number().numberBetween(MIN_POSITIVE_NUMBER, limit);
  }

  @Override
  public double positiveDecimal(double limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException(
          "El límite debe ser mayor que 0 (valor recibido: " + limit + ")");
    }
    int maxInt = (int) Math.ceil(limit);
    double randomScore = faker.number().randomDouble(2, 0, maxInt);
    return Math.min(randomScore, limit);
  }
}
