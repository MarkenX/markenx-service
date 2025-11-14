package com.udla.markenx.application.factories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.core.models.Student;
import com.udla.markenx.core.models.StudentTask;
import com.udla.markenx.core.models.Task;

import com.udla.markenx.application.builders.StudentTaskBuilder;
import com.udla.markenx.application.ports.out.data.generators.random.RandomNumberGeneratorPort;

@Component
public class RandomStudentTaskFactory {
  private static final int MAX_ATTEMPTS = 10;

  private final StudentTaskBuilder builder;
  private final RandomNumberGeneratorPort numberGenerator;
  private final RandomAttemptFactory attemptFactory;

  public RandomStudentTaskFactory(
      StudentTaskBuilder builder,
      RandomNumberGeneratorPort numberGenerator,
      RandomAttemptFactory attemptFactory) {
    this.builder = builder;
    this.numberGenerator = numberGenerator;
    this.attemptFactory = attemptFactory;
  }

  public StudentTask createRandomStudentTask(Task task, Student student) {
    StudentTask studentTask = builder.reset().setTask(task).setStudent(student).build();

    List<Attempt> attempts = attemptFactory.createRandomAttemptsUpTo(studentTask, task.getMinScoreToPass(),
        MAX_ATTEMPTS);
    studentTask.addAttempts(attempts);

    return studentTask;
  }

  public List<StudentTask> createRandomStudentTasks(int count, List<Task> tasks, List<Student> students) {
    List<StudentTask> studentTasks = new ArrayList<>();

    if (tasks == null || tasks.isEmpty() || students == null || students.isEmpty()) {
      return studentTasks;
    }

    for (Student student : students) {
      for (Task task : tasks) {
        for (int i = 0; i < count; i++) {
          StudentTask studentTask = createRandomStudentTask(task, student);
          studentTasks.add(studentTask);
        }
      }
    }

    return studentTasks;
  }

  public List<StudentTask> createRandomStudentTasksUpTo(int maxCount, List<Task> tasks, List<Student> students) {
    if (maxCount <= 0) {
      throw new IllegalArgumentException("El límite debe ser mayor que cero");
    }

    int count = numberGenerator.positiveIntegerBetween(1, maxCount);
    return createRandomStudentTasks(count, tasks, students);
  }
}
