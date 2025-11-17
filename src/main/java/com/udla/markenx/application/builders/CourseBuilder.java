package com.udla.markenx.application.builders;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.generators.random.RandomCourseDataGeneratorPort;
import com.udla.markenx.core.models.Course;

@Component
public class CourseBuilder {

  private final RandomCourseDataGeneratorPort randomGenerator;

  private UUID academicTermId;
  private int academicTermYear;
  private String name;

  public CourseBuilder reset() {
    this.academicTermId = null;
    this.academicTermYear = -1;
    this.name = null;
    return this;
  }

  public CourseBuilder(RandomCourseDataGeneratorPort randomGenerator) {
    this.randomGenerator = randomGenerator;
  }

  public CourseBuilder setAcademicTermId(UUID academicTermId) {
    this.academicTermId = academicTermId;
    return this;
  }

  public CourseBuilder setAcademicTermYear(int academicTermYear) {
    this.academicTermYear = academicTermYear;
    return this;
  }

  public CourseBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public CourseBuilder randomName() {
    this.name = randomGenerator.name();
    return this;
  }

  public Course build() {
    return new Course(academicTermId, academicTermYear, name);
  }
}
