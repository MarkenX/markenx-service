package com.udla.markenx.core.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class AcademicPeriod {
  private Long id;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final String label;

  private final List<Course> courses;

  public AcademicPeriod(LocalDate startDate, LocalDate endDate, String label) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.label = label;
    this.courses = new ArrayList<>();
  }

  public AcademicPeriod(
      long id,
      LocalDate startDate,
      LocalDate endDate,
      String label,
      List<Course> courses) {
    this.id = id;
    this.startDate = startDate;
    this.endDate = endDate;
    this.label = label;
    this.courses = courses;
  }

  public boolean addCourse(Course course) {
    if (course == null) {
      return false;
    }
    return this.courses.add(course);
  }
}
