package com.udla.markenx.application.builders;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.generators.random.RandomStudentDataGeneratorPort;
import com.udla.markenx.core.models.Student;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StudentBuilder {

  private final RandomStudentDataGeneratorPort randomGenerator;

  private UUID enrolledCourseId;
  private String firstName;
  private String lastName;
  private String academicEmail;

  public StudentBuilder reset() {
    this.enrolledCourseId = null;
    this.firstName = null;
    this.lastName = null;
    this.academicEmail = null;
    return this;
  }

  // #region Setters

  public StudentBuilder setEnrolledCourseId(UUID courseId) {
    this.enrolledCourseId = courseId;
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

  public StudentBuilder setAcademicEmail(String email) {
    this.academicEmail = email;
    return this;
  }

  // #endregion

  // #region Random Generators

  public StudentBuilder randomFirstName() {
    this.firstName = randomGenerator.firstName();
    return this;
  }

  public StudentBuilder randomLastName() {
    this.lastName = randomGenerator.lastName();
    return this;
  }

  public StudentBuilder randomEmail() {
    this.academicEmail = randomGenerator.email();
    return this;
  }

  // #endregion

  public Student build() {
    return new Student(
        enrolledCourseId,
        firstName,
        lastName,
        academicEmail);
  }
}
