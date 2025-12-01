package com.udla.markenx.classroom.application.seeders;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.commands.academicterm.CreateAcademicTermCommand;
import com.udla.markenx.classroom.application.services.AcademicTermService;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@Order(1)
@RequiredArgsConstructor
public class RandomAcademicTermSeeder implements CommandLineRunner {
  // private static final int MAX_COURSES = 4;

  private final AcademicTermService service;

  @Override
  @Transactional
  public void run(String... args) {
    var startDate = LocalDate.of(2025, 1, 15);
    var endDate = LocalDate.of(2025, 6, 30);
    int year = startDate.getYear();

    var command = new CreateAcademicTermCommand(startDate, endDate, year, "SYSTEM");
    service.createHistoricalAcademicTerm(command);
  }

  // public void generateRandomAcademicTermWithCourses(LocalDate start, LocalDate
  // end) {
  // int year = start.getYear();

  // var command = new CreateAcademicTermCommand(start, end, year);
  // AcademicTerm academicTerm = service.createAcademicTerm(command);

  // int courseCount = numberGenerator.positiveIntegerBetween(1, MAX_COURSES);
  // for (int i = 0; i < courseCount; i++) {
  // Course course = courseFactory.createRandomCourse(academicTerm.getId(),
  // academicTerm.getAcademicYear(),
  // academicTerm.getEndOfTerm());
  // academicTerm.addCourse(course);
  // }
  // }
}
