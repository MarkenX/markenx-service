package com.udla.markenx.application.factories;

import java.util.ArrayList;
import java.util.List;

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

  public Student createRandomStudent() {
    return studentBuilder
        .reset()
        .generateId()
        .randomFirstName()
        .randomLastName()
        .build();
  }

  public List<Student> createRandomStudents(int count) {
    List<Student> students = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      students.add(createRandomStudent());
    }
    return students;
  }

  public List<Student> createRandomStudentsUpTo(int maxCount) {
    if (maxCount <= 0) {
      throw new IllegalArgumentException("El límite debe ser mayor que cero");
    }

    int count = numberGenerator.positiveIntegerBetween(1, maxCount);
    return createRandomStudents(count);
  }
}
