package com.udla.markenx.application.seeders;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.AcademicPeriod;

import com.udla.markenx.application.factories.RandomAcademicPeriodFactory;
import com.udla.markenx.application.ports.out.persistance.repositories.AcademicPeriodRepositoryPort;

@Component
@Profile("dev")
public class AcademicPeriodSeeder implements CommandLineRunner {

	private final RandomAcademicPeriodFactory factory;
	private final AcademicPeriodRepositoryPort repository;

	public AcademicPeriodSeeder(
			RandomAcademicPeriodFactory builder,
			AcademicPeriodRepositoryPort repository) {
		this.factory = builder;
		this.repository = repository;
	}

	@Override
	public void run(String... args) {
		System.out.println("Starting database seeding...");

		LocalDate start = LocalDate.of(2025, 9, 1);
		LocalDate end = LocalDate.of(2026, 2, 1);
		AcademicPeriod period = factory.createRandomAcademicPeriod(start, end);
		repository.save(period);

		System.out.println("Database seeding complete!");
	}
}
