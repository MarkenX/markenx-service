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

import com.udla.markenx.classroom.academicterms.application.ports.out.persistance.repositories.AcademicTermRepositoryPort;
import com.udla.markenx.classroom.academicterms.domain.model.AcademicTerm;
import com.udla.markenx.classroom.application.builders.CourseBuilder;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomNumberGeneratorPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.domain.models.Course;

/**
 * Seeder for Course entities.
 * Creates courses associated with existing academic terms using random data.
 * Runs second in the seeding order.
 */
@Component
@Profile("dev")
@Order(2)
public class CourseSeeder implements CommandLineRunner {

  private static final int MIN_COURSES = 3;
  private static final int MAX_COURSES = 6;

  private final CourseBuilder courseBuilder;
  private final CourseRepositoryPort courseRepository;
  private final AcademicTermRepositoryPort academicTermRepository;
  private final RandomNumberGeneratorPort numberGenerator;

  public CourseSeeder(
      CourseBuilder courseBuilder,
      CourseRepositoryPort courseRepository,
      AcademicTermRepositoryPort academicTermRepository,
      RandomNumberGeneratorPort numberGenerator) {
    this.courseBuilder = courseBuilder;
    this.courseRepository = courseRepository;
    this.academicTermRepository = academicTermRepository;
    this.numberGenerator = numberGenerator;
  }

  @Override
  @Transactional
  public void run(String... args) {
    System.out.println("[2/4] Seeding Courses...");

    try {
      // Check if already seeded
      if (courseRepository.findAll(Pageable.unpaged()).getTotalElements() > 0) {
        System.out.println("  ✓ Courses already seeded, skipping...");
        return;
      }

      // Get academic terms
      Page<AcademicTerm> termsPage = academicTermRepository.findAllPaged(Pageable.unpaged());
      List<AcademicTerm> terms = termsPage.getContent();

      if (terms.isEmpty()) {
        System.err.println("  ✗ No Academic Terms found. Run AcademicTermSeeder first.");
        return;
      }

      // Create courses for EACH academic term (past and present)
      int totalCourses = 0;

      for (AcademicTerm term : terms) {
        UUID termId = term.getId();
        int courseCount = numberGenerator.positiveIntegerBetween(MIN_COURSES, MAX_COURSES);

        System.out.println("  Creating " + courseCount + " courses for " + term.getLabel() + "...");

        for (int i = 0; i < courseCount; i++) {
          Course course = courseBuilder
              .reset()
              .setAcademicTermId(termId)
              .randomName()
              .build();

          Course savedCourse = courseRepository.save(course);
          totalCourses++;
          System.out.println("  ✓ Created: " + savedCourse.getCode() + " - " + savedCourse.getName());
        }
      }

      System.out.println("  Total courses created: " + totalCourses);

      System.out.println("[2/4] ✓ Courses seeded successfully!\n");
    } catch (Exception e) {
      System.err.println("[2/4] ✗ ERROR seeding Courses: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}
