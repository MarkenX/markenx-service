package com.udla.markenx.classroom.application.seeders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.factories.RandomTaskFactory;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicTermRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort;
import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.domain.models.Task;

/**
 * Seeder for Task entities.
 * Creates tasks with random data associated with existing courses.
 * Runs fourth in the seeding order.
 */
@Component
@Profile("dev")
@Order(4)
public class TaskSeeder implements CommandLineRunner {

  private static final int MIN_TASKS_PER_COURSE = 3;
  private static final int MAX_TASKS_PER_COURSE = 8;

  private final RandomTaskFactory taskFactory;
  private final TaskRepositoryPort taskRepository;
  private final CourseRepositoryPort courseRepository;
  private final AcademicTermRepositoryPort academicTermRepository;
  private final RandomNumberGeneratorPort numberGenerator;

  public TaskSeeder(
      RandomTaskFactory taskFactory,
      TaskRepositoryPort taskRepository,
      CourseRepositoryPort courseRepository,
      AcademicTermRepositoryPort academicTermRepository,
      RandomNumberGeneratorPort numberGenerator) {
    this.taskFactory = taskFactory;
    this.taskRepository = taskRepository;
    this.courseRepository = courseRepository;
    this.academicTermRepository = academicTermRepository;
    this.numberGenerator = numberGenerator;
  }

  @Override
  @Transactional
  public void run(String... args) {
    System.out.println("[4/4] Seeding Tasks...");

    try {
      // Check if already seeded
      if (taskRepository.findAll(Pageable.unpaged()).getTotalElements() > 0) {
        System.out.println("  ✓ Tasks already seeded, skipping...");
        return;
      }

      // Get courses
      Page<Course> coursesPage = courseRepository.findAll(Pageable.unpaged());
      List<Course> courses = coursesPage.getContent();

      if (courses.isEmpty()) {
        System.err.println("  ✗ No Courses found. Run CourseSeeder first.");
        return;
      }

      // Create tasks for each course
      int totalTasks = 0;

      for (Course course : courses) {
        UUID courseId = course.getId();

        // Get academic term to set proper due dates
        AcademicTerm term = academicTermRepository.findById(course.getAcademicTermId())
            .orElseThrow(() -> new RuntimeException("Academic term not found for course: " + course.getId()));

        LocalDate termStartDate = term.getStartOfTerm();
        LocalDate termEndDate = term.getEndOfTerm();
        int academicYear = term.getAcademicYear();

        int taskCount = numberGenerator.positiveIntegerBetween(MIN_TASKS_PER_COURSE, MAX_TASKS_PER_COURSE);

        // Para seeders, SIEMPRE usar tareas históricas que permiten fechas en el pasado
        // Esto permite simular datos tanto de períodos pasados como actuales
        System.out.println("  Creating " + taskCount + " tasks for course: " + course.getName() +
            " [" + term.getLabel() + "]...");

        List<Task> tasks = taskFactory.createHistoricalTasks(courseId, academicYear, taskCount,
            termStartDate, termEndDate);

        for (Task task : tasks) {
          Task savedTask = taskRepository.save(task);
          totalTasks++;
          if (totalTasks <= 5 || totalTasks % 5 == 0) {
            System.out.println("  ✓ Created: " + savedTask.getCode() + " - " + savedTask.getTitle() +
                " (Due: " + savedTask.getDueDate() + ", Max attempts: " + savedTask.getMaxAttempts() + ")");
          }
        }
      }

      System.out.println("  Total tasks created: " + totalTasks);

      System.out.println("[4/4] ✓ Tasks seeded successfully!\n");
      System.out.println("=".repeat(50));
      System.out.println("✓ ALL SEEDERS COMPLETED SUCCESSFULLY!");
      System.out.println("=".repeat(50));
    } catch (Exception e) {
      System.err.println("[4/4] ✗ ERROR seeding Tasks: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}
