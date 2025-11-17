package com.udla.markenx.infrastructure.out.data.generators.random.faker.adapters;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.udla.markenx.application.ports.out.data.generators.random.RandomCourseDataGeneratorPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FakerRandomCourseDataGenerator implements RandomCourseDataGeneratorPort {
  private final Faker faker;

  @Override
  public String name() {
    return faker.pokemon().name();
  }
}
