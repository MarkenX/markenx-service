package com.udla.markenx.infrastructure.out.data.generators.random.faker.adapters;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.application.ports.out.data.generators.random.RandomDateGeneratorPort;

@Component
public class FakerRandomDateGenerator implements RandomDateGeneratorPort {

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

  @Override
  public LocalDate dateInRange(LocalDate from, LocalDate to) {
    Date fromDate = convertLocalDateToDate(from);
    Date toDate = convertLocalDateToDate(to);

    Date randomDate = faker.date().between(fromDate, toDate);
    return convertDateToLocalDate(randomDate);
  }

  private LocalDate convertDateToLocalDate(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  private Date convertLocalDateToDate(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }
}
