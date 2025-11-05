package com.udla.markenx.infrastructure.out.data.random.generators.faker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.javafaker.Faker;

@Configuration
public class FakerConfig {

  @Bean
  public Faker faker() {
    return new Faker();
  }
}