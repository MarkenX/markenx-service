package com.udla.markenx.infrastructure.out.database.seed;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.udla.markenx.infrastructure.out.persistance.jpa.entities.StudentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.jpa.entities.TaskJpaEntity;

@Component
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    private final StudentSeeder studentSeeder;
    private final TaskSeeder taskSeeder;
    private final AttemptSeeder attemptSeeder;

    public DatabaseSeeder(StudentSeeder studentSeeder, TaskSeeder taskSeeder, AttemptSeeder attemptSeeder) {
        this.studentSeeder = studentSeeder;
        this.taskSeeder = taskSeeder;
        this.attemptSeeder = attemptSeeder;
    }

    @Override
    public void run(String... args) {
        System.out.println("Starting database seeding...");
        List<StudentJpaEntity> students = studentSeeder.seed();
        List<TaskJpaEntity> attempts = taskSeeder.seed(students);
        attemptSeeder.seed(attempts);
        System.out.println("Database seeding complete!");
    }
}
