package com.udla.markenx.application.builders;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.random.generators.RandomStudentDataGeneratorPort;
import com.udla.markenx.core.models.Student;

@Component
public class StudentBuilder {

  private final RandomStudentDataGeneratorPort randomGenerator;

  private String firstName;
  private String lastName;

  public StudentBuilder(RandomStudentDataGeneratorPort randomGenerator) {
    this.randomGenerator = randomGenerator;
  }

  public StudentBuilder reset() {
    this.firstName = null;
    this.lastName = null;
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

  public Student build() {
    return new Student(firstName, lastName);
  }
}
