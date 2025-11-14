package com.udla.markenx.application.factories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.builders.AttemptBuilder;
import com.udla.markenx.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.application.ports.out.persistance.repositories.StudentAssignmentRepositoryPort;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.core.models.Student;
import com.udla.markenx.core.models.Task;

@Component
public class RandomAttemptFactory {

  private final AttemptBuilder attemptBuilder;
  private final RandomNumberGeneratorPort numberGenerator;
  private final StudentAssignmentRepositoryPort studentAssignmentRepository;
  private final RandomStudentTaskFactory studentAssignmentFactory;

  public RandomAttemptFactory(
      AttemptBuilder attemptBuilder,
      RandomNumberGeneratorPort numberGenerator,
      StudentAssignmentRepositoryPort studentAssignmentRepository,
      RandomStudentTaskFactory studentAssignmentFactory) {
    this.attemptBuilder = attemptBuilder;
    this.numberGenerator = numberGenerator;
    this.studentAssignmentRepository = studentAssignmentRepository;
    this.studentAssignmentFactory = studentAssignmentFactory;
  }

  public Attempt createRandomAttempt(Long studentId, Task task) {
    // ensure StudentAssignment exists for this assignment (task) + student
    com.udla.markenx.core.interfaces.StudentAssignment sa = studentAssignmentRepository
        .getByAssignmentIdAndStudentId(task.getId(), studentId);
    if (sa == null) {
      sa = studentAssignmentFactory.create(task.getId(), studentId);
      sa = studentAssignmentRepository.create(sa);
    }

    Attempt attempt = attemptBuilder
        .reset()
        .setTaskMinScoreToPass(task.getMinScoreToPass())
        .randomDate(task.getCreatedDate(), task.getDueDate())
        .randomScore()
        .randomDuration()
        .setStudentAssignmentId(sa.getId())
        .build();
    return attempt;
  }

  public List<Attempt> createRandomAttemptsForStudentsAndTask(
      int maxAttempts, List<Student> students, Task task) {

    List<Attempt> attempts = new ArrayList<>();

    if (students == null || students.isEmpty() || task == null) {
      return attempts;
    }

    for (Student student : students) {
      Long studentId = student.getId();
      if (studentId == null) {
        continue;
      }

      int attemptCount = numberGenerator.positiveIntegerBetween(1, maxAttempts);

      for (int i = 0; i < attemptCount; i++) {
        Attempt attempt = createRandomAttempt(studentId, task);
        attempts.add(attempt);
      }
    }

    return attempts;
  }

}
