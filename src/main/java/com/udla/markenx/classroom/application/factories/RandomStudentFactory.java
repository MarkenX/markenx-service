package com.udla.markenx.classroom.application.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.application.builders.StudentBuilder;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.classroom.core.models.Student;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RandomStudentFactory {
  private static final int MAX_TASKS = 30;

  private final StudentBuilder studentBuilder;
  private final RandomStudentTaskFactory randomStudentTaskFactory;
  private final RandomNumberGeneratorPort randomNumberGenerator;

  public Student createRandomStudent(UUID courseId) {
    Student student = studentBuilder
        .reset()
        .setEnrolledCourseId(courseId)
        .randomFirstName()
        .randomLastName()
        .randomEmail()
        .build();
    return student;
  }

  public List<Student> createRandomStudents(UUID courseId, int count) {
    List<Student> students = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      students.add(createRandomStudent(courseId));
    }
    return students;
  }

  public List<Student> createRandomStudentsUpTo(UUID courseId, int maxCount) {
    // if (maxCount <= 0) {
    // throw new IllegalArgumentException("El límite debe ser mayor que cero");
    // }

    int count = randomNumberGenerator.positiveIntegerBetween(1, maxCount);
    return createRandomStudents(courseId, count);
  }
}
