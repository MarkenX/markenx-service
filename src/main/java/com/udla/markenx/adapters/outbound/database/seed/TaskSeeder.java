package com.udla.markenx.adapters.outbound.database.seed;

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
import com.udla.markenx.adapters.outbound.persistance.jpa.entity.StudentJpaEntity;
import com.udla.markenx.adapters.outbound.persistance.jpa.entity.TaskJpaEntity;
import com.udla.markenx.adapters.outbound.persistance.jpa.repository.TaskJpaRepository;
import com.udla.markenx.domain.model.AssignmentStatus;

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
                TaskJpaEntity task = new TaskJpaEntity(
                        faker.job().title(),
                        faker.lorem().sentence(),
                        AssignmentStatus.values()[faker.random().nextInt(AssignmentStatus.values().length)],
                        localFutureDate,
                        faker.number().numberBetween(0, 3),
                        3,
                        student);
                TaskJpaEntity saved = taskRepository.save(task);
                tasks.add(saved);
            }
        }
        return tasks;
    }
}
