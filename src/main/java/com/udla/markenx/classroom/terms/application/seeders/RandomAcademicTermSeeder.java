package com.udla.markenx.classroom.terms.application.seeders;

import java.time.LocalDate;

import com.udla.markenx.classroom.terms.application.services.ManageTermsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.terms.application.commands.CreateCommand;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@Order(1)
@RequiredArgsConstructor
public class RandomAcademicTermSeeder implements CommandLineRunner {

  private final ManageTermsService service;

  @Override
  @Transactional
  public void run(String... args) {
    var startDate = LocalDate.of(2025, 1, 15);
    var endDate = LocalDate.of(2025, 6, 30);
    int year = startDate.getYear();

    var command = new CreateCommand(startDate, endDate, year, null, true);
    service.createTerm(command);
  }
}
