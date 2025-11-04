package com.udla.markenx.infrastructure.out.persistance.database.utils.generators.classes.faker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.infrastructure.out.persistance.database.utils.generators.interfaces.RandomDateGenerator;

@Component
public class FakerRandomDateGenerator implements RandomDateGenerator {

  private static final int MIN_DAYS_OFFSET = 1;

  private final Faker faker;

  public FakerRandomDateGenerator(Faker faker) {
    this.faker = faker;
  }

  @Override
  public LocalDate dateInFuture(int maxDaysInFuture) {
    int safeMaxDays = Math.max(MIN_DAYS_OFFSET, maxDaysInFuture);
    Date futureDate = faker.date().future(safeMaxDays, TimeUnit.DAYS);
    return convertDateToLocalDate(futureDate);
  }

  @Override
  public LocalDate dateInPast(int maxDaysInPast) {
    int safeMaxDays = Math.max(MIN_DAYS_OFFSET, maxDaysInPast);
    Date pastDate = faker.date().past(safeMaxDays, TimeUnit.DAYS);
    return convertDateToLocalDate(pastDate);
  }

  private LocalDate convertDateToLocalDate(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }
}
