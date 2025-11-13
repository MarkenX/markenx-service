package com.udla.markenx.application.factories;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.builders.AcademicTermBuilder;
import com.udla.markenx.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.core.models.AcademicTerm;
import com.udla.markenx.core.models.Course;

@Component
public class RandomAcademicPeriodFactory {
  private static final int MAX_COURSES = 4;

  private final RandomCourseFactory courseFactory;
  private final RandomNumberGeneratorPort numberGenerator;
  private final AcademicTermBuilder academicPeriodBuilder;

  public RandomAcademicPeriodFactory(
      RandomCourseFactory courseFactory,
      RandomNumberGeneratorPort numberGenerator,
      AcademicTermBuilder academicPeriodBuilder) {
    this.courseFactory = courseFactory;
    this.numberGenerator = numberGenerator;
    this.academicPeriodBuilder = academicPeriodBuilder;
  }

  public AcademicTerm createRandomAcademicPeriod(LocalDate startDate, LocalDate endDate) {
    int year = startDate.getYear();
    int semesterNumber = startDate.getMonthValue() <= 6 ? 1 : 2;

    AcademicTerm academicPeriod = academicPeriodBuilder
        .reset()
        .setStartOfTerm(startDate)
        .setEndOfTerm(endDate)
        .setAcademicYear(year)
        .setSemesterNumber(semesterNumber)
        .build();

    int courseCount = numberGenerator.positiveIntegerBetween(1, MAX_COURSES);
    for (int i = 0; i < courseCount; i++) {
      Course course = courseFactory.createRandomCourse(academicPeriod.getEndOfTerm());
      academicPeriod.addCourse(course);
    }

    return academicPeriod;
  }
}
