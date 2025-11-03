package com.udla.markenx.api.outbound.database.seed;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.api.outbound.persistance.jpa.entity.StudentJpaEntity;
import com.udla.markenx.api.outbound.persistance.jpa.repository.StudentJpaRepository;

@Component
@Profile("dev")
public class StudentSeeder {
    private final StudentJpaRepository studentRepository;
    private final Faker faker = new Faker();

    private final List<StudentJpaEntity> persons = new ArrayList<>();

    public StudentSeeder(StudentJpaRepository personRepository) {
        this.studentRepository = personRepository;
    }

    public List<StudentJpaEntity> seed() {
        if (studentRepository.count() != 0)
            return studentRepository.findAll();

        for (int i = 0; i < 5; i++) {
            StudentJpaEntity person = new StudentJpaEntity(
                    faker.name().firstName(),
                    faker.name().lastName());

            StudentJpaEntity saved = studentRepository.save(person);
            persons.add(saved);
        }
        return persons;
    }

    public List<StudentJpaEntity> getPersons() {
        return persons;
    }
}
