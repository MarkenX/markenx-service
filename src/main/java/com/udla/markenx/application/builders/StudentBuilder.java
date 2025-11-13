package com.udla.markenx.application.builders;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.generators.EntityIdGenerator;
import com.udla.markenx.application.ports.out.data.generators.random.RandomStudentDataGeneratorPort;
import com.udla.markenx.core.models.Student;

@Component
public class StudentBuilder {

  private final RandomStudentDataGeneratorPort randomGenerator;
  private final EntityIdGenerator idGenerator;

  private Long id;
  private String firstName;
  private String lastName;

  public StudentBuilder(RandomStudentDataGeneratorPort randomGenerator, EntityIdGenerator idGenerator) {
    this.randomGenerator = randomGenerator;
    this.idGenerator = idGenerator;
  }

  public StudentBuilder reset() {
    this.firstName = null;
    this.lastName = null;
    this.id = null;
    return this;
  }

  public StudentBuilder setId(long id) {
    this.id = id;
    return this;
  }

  public StudentBuilder generateId() {
    this.id = idGenerator.nextId();
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
    return new Student(id, firstName, lastName);
  }
}
