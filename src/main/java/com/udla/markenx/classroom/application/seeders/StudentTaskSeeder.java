package com.udla.markenx.classroom.application.seeders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort;
import com.udla.markenx.classroom.domain.models.Student;
import com.udla.markenx.classroom.domain.models.StudentTask;
import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.TaskJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentAssignmentJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.TaskJpaRepository;
import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.mapper.util.ExternalReferenceMapperHelper;

/**
 * Seeder for StudentTask entities.
 * Creates StudentTask records (student-task assignments) for each task and
 * student combination.
 * Runs fifth in the seeding order.
 */
@Component
@Profile("dev")
@Order(5)
public class StudentTaskSeeder implements CommandLineRunner {

  private final StudentAssignmentJpaRepository studentAssignmentRepository;
  private final TaskRepositoryPort taskRepository;
  private final StudentRepositoryPort studentRepository;
  private final TaskJpaRepository taskJpaRepository;
  private final StudentJpaRepository studentJpaRepository;
  private final ExternalReferenceMapperHelper externalReferenceHelper;

  public StudentTaskSeeder(
      StudentAssignmentJpaRepository studentAssignmentRepository,
      TaskRepositoryPort taskRepository,
      StudentRepositoryPort studentRepository,
      TaskJpaRepository taskJpaRepository,
      StudentJpaRepository studentJpaRepository,
      ExternalReferenceMapperHelper externalReferenceHelper) {
    this.studentAssignmentRepository = studentAssignmentRepository;
    this.taskRepository = taskRepository;
    this.studentRepository = studentRepository;
    this.taskJpaRepository = taskJpaRepository;
    this.studentJpaRepository = studentJpaRepository;
    this.externalReferenceHelper = externalReferenceHelper;
  }

  @Override
  @Transactional
  public void run(String... args) {
    System.out.println("[5/6] Seeding Student Tasks...");

    try {
      // Check if already seeded
      if (studentAssignmentRepository.findAll(Pageable.unpaged()).getTotalElements() > 0) {
        System.out.println("  ✓ Student Tasks already seeded, skipping...");
        return;
      }

      // Get all tasks
      Page<Task> tasksPage = taskRepository.findAll(Pageable.unpaged());
      List<Task> tasks = tasksPage.getContent();

      if (tasks.isEmpty()) {
        System.err.println("  ✗ No Tasks found. Run TaskSeeder first.");
        return;
      }

      // Get all students
      Page<Student> studentsPage = studentRepository.findAll(Pageable.unpaged());
      List<Student> students = studentsPage.getContent();

      if (students.isEmpty()) {
        System.err.println("  ✗ No Students found. Run StudentSeeder first.");
        return;
      }

      // Group students by course
      java.util.Map<UUID, List<Student>> studentsByCourse = new java.util.HashMap<>();
      for (Student student : students) {
        studentsByCourse
            .computeIfAbsent(student.getEnrolledCourseId(), k -> new ArrayList<>())
            .add(student);
      }

      // Create StudentTask for each task and its enrolled students
      int totalStudentTasks = 0;
      for (Task task : tasks) {
        UUID courseId = task.getCourseId();
        List<Student> courseStudents = studentsByCourse.getOrDefault(courseId, new ArrayList<>());

        if (courseStudents.isEmpty()) {
          System.out.println("  ⚠ No students found for task: " + task.getCode());
          continue;
        }

        System.out.println("  Assigning task " + task.getCode() + " to " + courseStudents.size() + " students...");

        // Find TaskJpaEntity
        TaskJpaEntity taskEntity = taskJpaRepository.findAll(Pageable.unpaged())
            .getContent()
            .stream()
            .filter(t -> t.getExternalReference() != null &&
                task.getId().equals(t.getExternalReference().getPublicId()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Task entity not found: " + task.getCode()));

        for (Student student : courseStudents) {
          // Find StudentJpaEntity
          StudentJpaEntity studentEntity = studentJpaRepository.findAll(Pageable.unpaged())
              .getContent()
              .stream()
              .filter(s -> s.getExternalReference() != null &&
                  student.getId().equals(s.getExternalReference().getPublicId()))
              .findFirst()
              .orElseThrow(() -> new RuntimeException("Student entity not found: " + student.getCode()));

          // Create domain StudentTask
          StudentTask studentTask = new StudentTask(
              task,
              student.getId(),
              student.getSerialNumber(),
              "SYSTEM");

          // Calculate the correct assignment status based on task due date and attempts
          studentTask.updateStatus();

          // Convert to entity
          StudentTaskJpaEntity entity = new StudentTaskJpaEntity();
          entity.setExternalReference(
              externalReferenceHelper.createExternalReference(
                  studentTask.getId(),
                  null, // Code will be generated after save
                  "STUDENT_TASK"));
          entity.setStatus(studentTask.getEntityStatus());
          entity.setCurrentStatus(studentTask.getAssignmentStatus());
          entity.setAssignment(taskEntity);
          entity.setStudent(studentEntity);
          entity.setCreatedBy("SYSTEM");
          entity.setCreatedAt(java.time.LocalDateTime.now());
          entity.setUpdatedAt(java.time.LocalDateTime.now());
          entity.setUpdatedBy("SYSTEM");

          var savedEntity = studentAssignmentRepository.save(entity);
          totalStudentTasks++;

          if (totalStudentTasks <= 5 || totalStudentTasks % 20 == 0) {
            String code = externalReferenceHelper.extractCode(savedEntity.getExternalReference());
            System.out.println("  ✓ Created: " + code + " (Task: " + task.getCode() +
                ", Student: " + student.getCode() + ")");
          }
        }
      }

      System.out.println("  Total student tasks created: " + totalStudentTasks);
      System.out.println("[5/6] ✓ Student Tasks seeded successfully!\n");
    } catch (Exception e) {
      System.err.println("[5/6] ✗ ERROR seeding Student Tasks: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}
