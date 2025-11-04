package com.udla.markenx.application.factories;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.builders.StudentBuilder;
import com.udla.markenx.core.models.Course;
import com.udla.markenx.core.models.Student;
import com.udla.markenx.core.models.Task;

@Component
public class RandomCourseFactory {
  private final StudentBuilder studentBuilder;
  private final RandomTaskFactory taskFactory;

  public RandomCourseFactory(StudentBuilder studentBuilder, RandomTaskFactory taskFactory) {
    this.studentBuilder = studentBuilder;
    this.taskFactory = taskFactory;
  }

  public Course createRandomCourse(int studentCount, int taskCount) {
    Course course = new Course();

    for (int i = 0; i < studentCount; i++) {
      Student student = studentBuilder
          .reset()
          .randomFirstName()
          .randomLastName()
          .build();
      course.addStudent(student);
    }

    for (int i = 0; i < taskCount; i++) {
      // TODO: Lógica para crear tareas con y sin intentos
      Task task = taskFactory.createRandomTaskWithAttempts(i, null);
      course.addAssignment(task);
    }

    return course;
  }
}
