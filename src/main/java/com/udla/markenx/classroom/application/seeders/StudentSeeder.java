package com.udla.markenx.classroom.application.seeders;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.factories.RandomStudentFactory;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.domain.models.Student;
import com.udla.markenx.shared.application.port.out.auth.AuthenticationServicePort;

/**
 * Seeder for Student entities.
 * Creates students with random data associated with existing courses.
 * Runs third in the seeding order.
 */
@Component
@Profile("dev")
@Order(3)
public class StudentSeeder implements CommandLineRunner {

  private static final int MIN_STUDENTS_PER_COURSE = 5;
  private static final int MAX_STUDENTS_PER_COURSE = 15;
  private static final String DEFAULT_PASSWORD = "ChangeMe123!";
  private static final String STUDENT_ROLE = "STUDENT";

  private final RandomStudentFactory studentFactory;
  private final StudentRepositoryPort studentRepository;
  private final CourseRepositoryPort courseRepository;
  private final RandomNumberGeneratorPort numberGenerator;
  private final AuthenticationServicePort authenticationService;

  public StudentSeeder(
      RandomStudentFactory studentFactory,
      StudentRepositoryPort studentRepository,
      CourseRepositoryPort courseRepository,
      RandomNumberGeneratorPort numberGenerator,
      AuthenticationServicePort authenticationService) {
    this.studentFactory = studentFactory;
    this.studentRepository = studentRepository;
    this.courseRepository = courseRepository;
    this.numberGenerator = numberGenerator;
    this.authenticationService = authenticationService;
  }

  @Override
  @Transactional
  public void run(String... args) {
    System.out.println("[3/4] Seeding Students...");

    try {
      // Check if already seeded
      if (studentRepository.findAll(Pageable.unpaged()).getTotalElements() > 0) {
        System.out.println("  ✓ Students already seeded, skipping...");
        return;
      }

      // Get courses
      Page<Course> coursesPage = courseRepository.findAll(Pageable.unpaged());
      List<Course> courses = coursesPage.getContent();

      if (courses.isEmpty()) {
        System.err.println("  ✗ No Courses found. Run CourseSeeder first.");
        return;
      }

      // Create students for each course
      int totalStudents = 0;
      for (Course course : courses) {
        UUID courseId = course.getId();
        int studentCount = numberGenerator.positiveIntegerBetween(MIN_STUDENTS_PER_COURSE, MAX_STUDENTS_PER_COURSE);

        System.out.println("  Creating " + studentCount + " students for course: " + course.getName() + "...");

        List<Student> students = studentFactory.createRandomStudents(courseId, studentCount);

        for (Student student : students) {
          // Check if user already exists in Keycloak
          if (!authenticationService.userExists(student.getAcademicEmail())) {
            // Create user in Keycloak with STUDENT role
            authenticationService.createUser(
                student.getAcademicEmail(),
                DEFAULT_PASSWORD,
                student.getFirstName(),
                student.getLastName(),
                STUDENT_ROLE,
                false // Don't require password change for seeded data
            );
          }

          Student savedStudent = studentRepository.save(student);
          totalStudents++;
          if (totalStudents <= 5 || totalStudents % 5 == 0) {
            System.out.println("  ✓ Created: " + savedStudent.getCode() + " - " +
                savedStudent.getFirstName() + " " + savedStudent.getLastName() +
                " (" + savedStudent.getAcademicEmail() + ") [Password: " + DEFAULT_PASSWORD + "]");
          }
        }
      }

      System.out.println("  Total students created: " + totalStudents);
      System.out.println("  ℹ All students registered in Keycloak with password: " + DEFAULT_PASSWORD);

      System.out.println("[3/4] ✓ Students seeded successfully!\n");
    } catch (Exception e) {
      System.err.println("[3/4] ✗ ERROR seeding Students: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}
