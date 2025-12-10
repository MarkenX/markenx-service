package com.udla.markenx.classroom.terms.application.seeders;
// package com.udla.markenx.classroom.application.seeders;

// import java.time.LocalDate;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Profile;
// import org.springframework.core.annotation.Order;
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Transactional;

// import com.udla.markenx.classroom.application.builders.AcademicTermBuilder;
// import
// com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicTermRepositoryPort;
// import com.udla.markenx.classroom.domain.models.AcademicTerm;

// /**
// * Seeder for AcademicTerm entities.
// * Creates academic terms/periods without nested entities.
// * Runs first in the seeding order.
// */
// @Component
// @Profile("dev")
// @Order(1)
// public class AcademicTermSeeder implements CommandLineRunner {

// private final AcademicTermBuilder builder;
// private final AcademicTermRepositoryPort repository;

// public AcademicTermSeeder(
// AcademicTermBuilder builder,
// AcademicTermRepositoryPort repository) {
// this.builder = builder;
// this.repository = repository;
// }

// @Override
// @Transactional
// public void run(String... args) {
// System.out.println("[1/4] Seeding Academic Terms...");

// try {
// // Check if already seeded
// if
// (repository.findAllPaged(org.springframework.data.domain.Pageable.unpaged()).getTotalElements()
// > 0) {
// System.out.println(" ✓ Academic Terms already seeded, skipping...");
// return;
// }

// // Create 2025-1 period (PAST - for historical data simulation)
// AcademicTerm term2025_1 = builder
// .reset()
// .setStartOfTerm(LocalDate.of(2025, 1, 15))
// .setEndOfTerm(LocalDate.of(2025, 6, 30))
// .setAcademicYear(2025)
// .build();
// term2025_1 = repository.save(term2025_1);
// System.out.println(" ✓ Created: " + term2025_1.getCode() + " (" +
// term2025_1.getLabel() + ") [HISTORICAL]");

// // Create 2025-2 period (ACTIVE/FUTURE)
// AcademicTerm term2025_2 = builder
// .reset()
// .setStartOfTerm(LocalDate.of(2025, 9, 1))
// .setEndOfTerm(LocalDate.of(2026, 2, 1))
// .setAcademicYear(2025)
// .build();
// term2025_2 = repository.save(term2025_2);
// System.out.println(" ✓ Created: " + term2025_2.getCode() + " (" +
// term2025_2.getLabel() + ") [ACTIVE]");

// System.out.println("[1/4] ✓ Academic Terms seeded successfully!\n");
// } catch (Exception e) {
// System.err.println("[1/4] ✗ ERROR seeding Academic Terms: " +
// e.getMessage());
// e.printStackTrace();
// throw e;
// }
// }
// }
