package com.udla.markenx.application.factories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Course;
import com.udla.markenx.core.models.Student;
import com.udla.markenx.core.models.Task;

@Component
public class RandomCourseFactory {
  private static final int MAX_STUDENTS = 30;
  private static final int MAX_TASKS = 30;

  private final RandomStudentFactory studentFactory;
  private final RandomTaskFactory taskFactory;

  public RandomCourseFactory(
      RandomStudentFactory studentFactory,
      RandomTaskFactory taskFactory) {
    this.studentFactory = studentFactory;
    this.taskFactory = taskFactory;
  }

  public Course createRandomCourse(LocalDate endDate) {
    Course course = new Course();

    List<Student> students = studentFactory
        .createRandomStudentsUpTo(MAX_STUDENTS);
    course.addStudents(students);

    List<Task> tasks = taskFactory
        .createRandomTasksUpTo(MAX_TASKS, endDate);
    course.addAssignments(tasks);

    return course;
  }
}
