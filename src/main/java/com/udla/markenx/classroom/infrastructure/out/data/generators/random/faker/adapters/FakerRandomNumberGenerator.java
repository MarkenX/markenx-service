package com.udla.markenx.classroom.infrastructure.out.data.generators.random.faker.adapters;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomNumberGeneratorPort;

@Component
public class FakerRandomNumberGenerator implements RandomNumberGeneratorPort {

  private static final int MIN_POSITIVE_NUMBER = 0;

  private final Faker faker;

  public FakerRandomNumberGenerator(Faker faker) {
    this.faker = faker;
  }

  @Override
  public int positiveIntegerBetween(int min, int max) {
    validatePositiveLimit(min);
    if (max < min) {
      throw new IllegalArgumentException(
          String.format("El valor máximo (%d) no puede ser menor que el valor mínimo (%d).", max, min));
    }
    return faker.number().numberBetween(min, max);
  }

  @Override
  public int positiveInteger(int limit) {
    validatePositiveLimit(limit);
    return faker.number().numberBetween(MIN_POSITIVE_NUMBER, limit);
  }

  @Override
  public double positiveDecimal(double limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException(
          "El límite debe ser mayor que 0 (valor recibido: " + limit + ")");
    }
    double randomValue = faker.number().randomDouble(2, 0, 1);
    return randomValue * limit;
  }

  private void validatePositiveLimit(int value) {
    if (value < MIN_POSITIVE_NUMBER) {
      throw new IllegalArgumentException(
          String.format("El valor debe ser mayor o igual que %d (valor recibido: %d).", MIN_POSITIVE_NUMBER, value));
    }
  }
}
