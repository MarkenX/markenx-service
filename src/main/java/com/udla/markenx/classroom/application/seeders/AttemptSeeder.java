package com.udla.markenx.classroom.application.seeders;

import java.time.Duration;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.builders.AttemptBuilder;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.classroom.domain.models.Attempt;
import com.udla.markenx.classroom.domain.models.StudentTask;
import com.udla.markenx.classroom.domain.valueobjects.enums.AttemptStatus;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.AttemptJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentAssignmentJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.AttemptMapper;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.StudentTaskMapper;

/**
 * Seeder for Attempt entities.
 * Creates attempts for a percentage of student tasks with random scores.
 * Runs sixth (last) in the seeding order.
 */
@Component
@Profile("dev")
@Order(6)
public class AttemptSeeder implements CommandLineRunner {

  private static final int MIN_ATTEMPTS_PER_STUDENT_TASK = 0; // Some may have no attempts
  private static final int MAX_ATTEMPTS_RATIO = 1; // Max attempts based on task max attempts
  private static final double STUDENT_TASK_WITH_ATTEMPTS_PERCENTAGE = 0.6; // 60% of students will have attempts

  private final AttemptBuilder attemptBuilder;
  private final AttemptJpaRepository attemptRepository;
  private final AttemptMapper attemptMapper;
  private final StudentAssignmentJpaRepository studentAssignmentRepository;
  private final StudentTaskMapper studentTaskMapper;
  private final RandomNumberGeneratorPort numberGenerator;

  public AttemptSeeder(
      AttemptBuilder attemptBuilder,
      AttemptJpaRepository attemptRepository,
      AttemptMapper attemptMapper,
      StudentAssignmentJpaRepository studentAssignmentRepository,
      StudentTaskMapper studentTaskMapper,
      RandomNumberGeneratorPort numberGenerator) {
    this.attemptBuilder = attemptBuilder;
    this.attemptRepository = attemptRepository;
    this.attemptMapper = attemptMapper;
    this.studentAssignmentRepository = studentAssignmentRepository;
    this.studentTaskMapper = studentTaskMapper;
    this.numberGenerator = numberGenerator;
  }

  @Override
  @Transactional
  public void run(String... args) {
    System.out.println("[6/6] Seeding Attempts...");

    try {
      // Check if already seeded
      if (attemptRepository.findAll(Pageable.unpaged()).getTotalElements() > 0) {
        System.out.println("  ✓ Attempts already seeded, skipping...");
        return;
      }

      // Get all student tasks
      var studentTaskEntities = studentAssignmentRepository.findAll(Pageable.unpaged()).getContent();

      if (studentTaskEntities.isEmpty()) {
        System.err.println("  ✗ No Student Tasks found. Run StudentTaskSeeder first.");
        return;
      }

      System.out.println("  Creating attempts for ~" + (int) (STUDENT_TASK_WITH_ATTEMPTS_PERCENTAGE * 100) +
          "% of student tasks...");

      int totalAttempts = 0;
      int studentTasksWithAttempts = 0;

      for (var studentTaskEntity : studentTaskEntities) {
        // Cast to StudentTaskJpaEntity
        if (!(studentTaskEntity instanceof com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity)) {
          continue;
        }

        var studentTaskJpaEntity = (com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity) studentTaskEntity;
        StudentTask studentTask = studentTaskMapper.toDomain(studentTaskJpaEntity);

        // Only create attempts for a percentage of student tasks (to simulate reality)
        if (Math.random() > STUDENT_TASK_WITH_ATTEMPTS_PERCENTAGE) {
          continue;
        }

        UUID studentTaskId = studentTask.getId();
        Long studentSerialNumber = studentTaskJpaEntity.getStudent().getId();
        Long taskSerialNumber = studentTask.getAssignment().getSerialNumber();
        int maxAttempts = studentTask.getAssignment().getMaxAttempts();
        double minScoreToPass = studentTask.getAssignment().getMinScoreToPass();

        // Generate random number of attempts (between 0 and maxAttempts)
        int attemptCount = numberGenerator.positiveIntegerBetween(
            MIN_ATTEMPTS_PER_STUDENT_TASK,
            Math.min(maxAttempts, maxAttempts * MAX_ATTEMPTS_RATIO));

        if (attemptCount == 0) {
          continue;
        }

        studentTasksWithAttempts++;

        for (int i = 0; i < attemptCount; i++) {
          // Generate random score (0-100) and convert to 0-1 scale
          int scorePercentage = numberGenerator.positiveIntegerBetween(0, 100);
          double score = scorePercentage / 100.0;

          // Generate random time spent (5-60 minutes)
          int minutesSpent = numberGenerator.positiveIntegerBetween(5, 60);
          Duration timeSpent = Duration.ofMinutes(minutesSpent);

          // All attempts are completed (not interrupted)
          AttemptStatus attemptStatus = AttemptStatus.COMPLETED;

          Attempt attempt = attemptBuilder
              .reset()
              .setStudentTaskId(studentTaskId)
              .setStudentSequence(studentSerialNumber)
              .setTaskSequence(taskSerialNumber)
              .setTaskMinSocreToPass(minScoreToPass)
              .setScore(score)
              .setTimeSpent(timeSpent)
              .setAttemptStatus(attemptStatus)
              .build();

          var entity = attemptMapper.toEntity(attempt);
          entity.setStudentTask(studentTaskJpaEntity); // Set the required relationship
          var savedEntity = attemptRepository.save(entity);
          totalAttempts++;

          if (totalAttempts <= 5 || totalAttempts % 50 == 0) {
            Attempt saved = attemptMapper.toDomain(savedEntity);
            String result = saved.getResult() != null ? saved.getResult().name() : "INTERRUPTED";
            System.out.println("  ✓ Created: " + saved.getCode() +
                " (Score: " + String.format("%.0f", saved.getScore() * 100) + "/100, Result: " + result + ")");
          }
        }
      }

      System.out.println("  Total attempts created: " + totalAttempts);
      System.out.println("  Student tasks with attempts: " + studentTasksWithAttempts +
          " of " + studentTaskEntities.size());

      System.out.println("[6/6] ✓ Attempts seeded successfully!\n");
      System.out.println("=".repeat(50));
      System.out.println("✓ ALL SEEDERS COMPLETED SUCCESSFULLY!");
      System.out.println("  - Academic Terms: 2");
      System.out.println("  - Courses: Multiple per term");
      System.out.println("  - Students: Multiple per course");
      System.out.println("  - Tasks: Multiple per course");
      System.out.println("  - Student Tasks: All combinations");
      System.out.println("  - Attempts: Random for ~60% of student tasks");
      System.out.println("=".repeat(50));
    } catch (Exception e) {
      System.err.println("[6/6] ✗ ERROR seeding Attempts: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}
