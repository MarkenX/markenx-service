package com.udla.markenx.application.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.builders.StudentBuilder;
import com.udla.markenx.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.core.models.Student;

@Component
public class RandomStudentFactory {
  private final StudentBuilder studentBuilder;
  private final RandomNumberGeneratorPort numberGenerator;

  public RandomStudentFactory(
      StudentBuilder studentBuilder,
      RandomNumberGeneratorPort numberGenerator) {
    this.studentBuilder = studentBuilder;
    this.numberGenerator = numberGenerator;
  }

  public Student createRandomStudent(UUID courseId) {
    return studentBuilder
        .reset()
        .setCourseId(courseId)
        .randomFirstName()
        .randomLastName()
        .randomEmail()
        .build();
  }

  public List<Student> createRandomStudents(UUID courseId, int count) {
    List<Student> students = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      students.add(createRandomStudent(courseId));
    }
    return students;
  }

  public List<Student> createRandomStudentsUpTo(UUID courseId, int maxCount) {
    if (maxCount <= 0) {
      throw new IllegalArgumentException("El límite debe ser mayor que cero");
    }

    int count = numberGenerator.positiveIntegerBetween(1, maxCount);
    return createRandomStudents(courseId, count);
  }
}
