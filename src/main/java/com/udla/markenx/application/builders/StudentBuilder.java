package com.udla.markenx.application.builders;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.generators.random.RandomStudentDataGeneratorPort;
import com.udla.markenx.core.models.Student;

@Component
public class StudentBuilder {

  private final RandomStudentDataGeneratorPort randomGenerator;

  private UUID courseId;
  private String firstName;
  private String lastName;
  private String email;

  public StudentBuilder(RandomStudentDataGeneratorPort randomGenerator) {
    this.randomGenerator = randomGenerator;
  }

  public StudentBuilder reset() {
    this.firstName = null;
    this.lastName = null;
    return this;
  }

  public StudentBuilder setCourseId(UUID courseId) {
    this.courseId = courseId;
    return this;
  }

  public StudentBuilder setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public StudentBuilder setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public StudentBuilder setEmail(String email) {
    this.email = email;
    return this;
  }

  public StudentBuilder randomFirstName() {
    this.firstName = randomGenerator.firstName();
    return this;
  }

  public StudentBuilder randomLastName() {
    this.lastName = randomGenerator.lastName();
    return this;
  }

  public StudentBuilder randomEmail() {
    this.email = randomGenerator.email();
    return this;
  }

  public Student build() {
    return new Student(courseId, firstName, lastName, email);
  }
}
