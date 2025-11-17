package com.udla.markenx.classroom.application.factories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.application.builders.CourseBuilder;
import com.udla.markenx.classroom.core.models.Course;
import com.udla.markenx.classroom.core.models.Student;
import com.udla.markenx.classroom.core.models.Task;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RandomCourseFactory {
  private static final int MAX_STUDENTS = 30;
  private static final int MAX_TASKS = 30;

  private final RandomStudentFactory studentFactory;
  private final RandomTaskFactory taskFactory;
  private final RandomStudentTaskFactory randomStudentTaskFactory;
  private final CourseBuilder courseBuilder;

  public Course createRandomCourse(UUID academicTermId, int academicTermYear, LocalDate endDate) {
    Course course = courseBuilder
        .reset()
        .setAcademicTermId(academicTermId)
        .setAcademicTermYear(academicTermYear)
        .randomName()
        .build();

    List<Student> students = studentFactory
        .createRandomStudentsUpTo(course.getId(), MAX_STUDENTS);
    course.addStudents(students);

    List<Task> tasks = taskFactory
        .createRandomTasksUpTo(course.getId(), academicTermYear, MAX_TASKS, endDate);
    course.addAssignments(tasks);

    randomStudentTaskFactory.createRandomStudentTasksUpTo(2, tasks, students);

    return course;
  }
}
