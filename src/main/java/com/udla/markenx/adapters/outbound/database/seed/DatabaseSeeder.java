package com.udla.markenx.adapters.outbound.database.seed;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.udla.markenx.adapters.outbound.persistance.jpa.entity.StudentJpaEntity;

@Component
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    private final StudentSeeder studentSeeder;
    private final TaskSeeder taskSeeder;

    public DatabaseSeeder(StudentSeeder studentSeeder, TaskSeeder taskSeeder) {
        this.studentSeeder = studentSeeder;
        this.taskSeeder = taskSeeder;
    }

    @Override
    public void run(String... args) {
        System.out.println("Starting database seeding...");
        List<StudentJpaEntity> students = studentSeeder.seed();
        taskSeeder.seed(students);
        System.out.println("Database seeding complete!");
    }
}
