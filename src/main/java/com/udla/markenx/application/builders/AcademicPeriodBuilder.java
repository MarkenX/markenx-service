package com.udla.markenx.application.builders;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.application.ports.out.data.random.generators.RandomAcademicPeriodDataGeneratorPort;
import com.udla.markenx.core.models.AcademicPeriod;

@Component
public class AcademicPeriodBuilder {

  private final RandomAcademicPeriodDataGeneratorPort randomGenerator;

  private LocalDate startDate;
  private LocalDate endDate;
  private String label;

  public AcademicPeriodBuilder(RandomAcademicPeriodDataGeneratorPort randomGenerator) {
    this.randomGenerator = randomGenerator;
  }

  public AcademicPeriodBuilder reset() {
    this.startDate = null;
    this.endDate = null;
    this.label = null;
    return this;
  }

  public AcademicPeriodBuilder setStartDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  public AcademicPeriodBuilder setEndDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  public AcademicPeriodBuilder randomLabel() {
    this.label = randomGenerator.label();
    return this;
  }

  public AcademicPeriod build() {
    return new AcademicPeriod(startDate, endDate, label);
  }
}
