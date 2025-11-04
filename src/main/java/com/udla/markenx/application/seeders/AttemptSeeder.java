package com.udla.markenx.application.seeders;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.AttemptJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.AttemptJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;

@Component
@Profile("dev")
public class AttemptSeeder {
  private final AttemptJpaRepository attemptRepository;
  private final Faker faker = new Faker();

  private final List<AttemptJpaEntity> attempts = new ArrayList<>();

  public AttemptSeeder(AttemptJpaRepository attemptRepository) {
    this.attemptRepository = attemptRepository;
  }

  public List<AttemptJpaEntity> seed(List<TaskJpaEntity> tasks) {
    if (tasks == null || attemptRepository.count() != 0)
      return null;

    for (TaskJpaEntity task : tasks) {
      for (int i = 0; i < 10; i++) {
        Date futureDate = faker.date().future(30, TimeUnit.DAYS);
        LocalDate localFutureDate = Instant.ofEpochMilli(futureDate.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

        double randomScore = faker.number().randomDouble(2, 0, 10);

        long minutes = faker.number().numberBetween(1, 90);
        Duration duration = Duration.ofMinutes(minutes);

        // AttemptJpaEntity attempt = new AttemptJpaEntity(
        // randomScore,
        // localFutureDate,
        // duration,
        // task);

        // AttemptJpaEntity saved = attemptRepository.save(attempt);
        // attempts.add(saved);
      }
    }
    return attempts;
  }
}
