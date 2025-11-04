package com.udla.markenx.infrastructure.out.database.generators.classes.faker;

import com.github.javafaker.Faker;
import com.udla.markenx.infrastructure.out.database.generators.interfaces.RandomNumberGenerator;

public class FakerRandomNumberGenerator implements RandomNumberGenerator {

  private static final int MIN_POSITIVE_NUMBER = 1;

  private final Faker faker;

  public FakerRandomNumberGenerator(Faker faker) {
    this.faker = faker;
  }

  @Override
  public int positiveInteger(int max) {
    int safeMax = Math.max(MIN_POSITIVE_NUMBER, max);
    return faker.number().numberBetween(MIN_POSITIVE_NUMBER, safeMax);
  }

  @Override
  public double positiveDecimal(double max) {
    int maxInt = (int) Math.ceil(max);
    double randomScore = faker.number().randomDouble(2, 0, maxInt);
    return Math.min(randomScore, max);
  }
}
