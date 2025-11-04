package com.udla.markenx.infrastructure.out.persistance.database.seeders;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.infrastructure.out.persistance.adapters.jpa.repositories.TaskJpaRepository;
import com.udla.markenx.infrastructure.out.persistance.database.entities.StudentJpaEntity;
import com.udla.markenx.infrastructure.out.persistance.database.entities.TaskJpaEntity;

@Component
@Profile("dev")
public class TaskSeeder {
    private final TaskJpaRepository taskRepository;
    private final Faker faker = new Faker();

    private final List<TaskJpaEntity> tasks = new ArrayList<>();

    public TaskSeeder(TaskJpaRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskJpaEntity> seed(List<StudentJpaEntity> students) {
        if (students == null || taskRepository.count() != 0)
            return null;

        for (StudentJpaEntity student : students) {
            for (int i = 0; i < 10; i++) {
                Date futureDate = faker.date().future(30, TimeUnit.DAYS);
                LocalDate localFutureDate = Instant.ofEpochMilli(futureDate.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                TaskJpaEntity task = new TaskJpaEntity();
                task.setTitle(faker.app().name());
                task.setSummary(faker.lorem().paragraph(2));
                task.setCurrentStatus(
                        AssignmentStatus.values()[faker.random().nextInt(AssignmentStatus.values().length)]);
                task.setDueDate(localFutureDate);
                task.setActiveAttempt(faker.number().numberBetween(0, 3));
                task.setMaxAttempts(3);
                task.setStudent(student);
                TaskJpaEntity saved = taskRepository.save(task);
                tasks.add(saved);
            }
        }
        return tasks;
    }
}
