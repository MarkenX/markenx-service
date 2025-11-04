package com.udla.markenx.application.factories;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.builders.AcademicPeriodBuilder;
import com.udla.markenx.application.ports.out.data.random.generators.RandomNumberGeneratorPort;
import com.udla.markenx.core.models.AcademicPeriod;
import com.udla.markenx.core.models.Course;

@Component
public class RandomAcademicPeriodFactory {
  private static final int MAX_COURSES = 4;

  private final RandomCourseFactory courseFactory;
  private final RandomNumberGeneratorPort numberGenerator;
  private final AcademicPeriodBuilder academicPeriodBuilder;

  public RandomAcademicPeriodFactory(
      RandomCourseFactory courseFactory,
      RandomNumberGeneratorPort numberGenerator,
      AcademicPeriodBuilder academicPeriodBuilder) {
    this.courseFactory = courseFactory;
    this.numberGenerator = numberGenerator;
    this.academicPeriodBuilder = academicPeriodBuilder;
  }

  public AcademicPeriod createRandomAcademicPeriod(LocalDate startDate, LocalDate endDate) {
    AcademicPeriod academicPeriod = academicPeriodBuilder
        .reset()
        .setStartDate(startDate)
        .setEndDate(endDate)
        .randomLabel()
        .build();

    int courseCount = numberGenerator.positiveInteger(MAX_COURSES);
    for (int i = 0; i < courseCount; i++) {
      Course course = courseFactory.createRandomCourse(academicPeriod.getEndDate());
      academicPeriod.addCourse(course);
    }

    return academicPeriod;
  }
}
