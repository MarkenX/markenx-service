package com.udla.markenx.application.factories;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.builders.StudentBuilder;
import com.udla.markenx.application.ports.out.data.random.generators.RandomNumberGeneratorPort;
import com.udla.markenx.core.models.Course;
import com.udla.markenx.core.models.Student;
import com.udla.markenx.core.models.Task;

@Component
public class RandomCourseFactory {
  private static final int MAX_STUDENTS = 30;
  private static final int MAX_TASKS = 30;

  private final StudentBuilder studentBuilder;
  private final RandomTaskFactory taskFactory;
  private final RandomNumberGeneratorPort numberGenerator;

  public RandomCourseFactory(
      StudentBuilder studentBuilder,
      RandomTaskFactory taskFactory,
      RandomNumberGeneratorPort numberGenerator) {
    this.studentBuilder = studentBuilder;
    this.taskFactory = taskFactory;
    this.numberGenerator = numberGenerator;
  }

  public Course createRandomCourse(LocalDate endDate) {
    Course course = new Course();

    int studentCount = numberGenerator.positiveInteger(MAX_STUDENTS);
    for (int i = 0; i < studentCount; i++) {
      Student student = studentBuilder
          .reset()
          .randomFirstName()
          .randomLastName()
          .build();
      course.addStudent(student);
    }

    int taskCount = numberGenerator.positiveInteger(MAX_TASKS);
    for (int i = 0; i < taskCount; i++) {
      Task task = taskFactory.createRandomTaskWithAttempts(endDate);
      course.addAssignment(task);
    }

    return course;
  }
}
