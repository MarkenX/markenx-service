package com.udla.markenx.application.builders;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.AcademicTerm;

@Component
public class AcademicTermBuilder {

  private LocalDate startDate;
  private LocalDate endDate;
  private int year;

  public AcademicTermBuilder reset() {
    this.startDate = null;
    this.endDate = null;
    this.year = 0;
    return this;
  }

  public AcademicTermBuilder setStartDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  public AcademicTermBuilder setEndDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  public AcademicTermBuilder setYear(int year) {
    this.year = year;
    return this;
  }

  public AcademicTermBuilder randomDates() {
    this.startDate = LocalDate.now().plusMonths((long) (Math.random() * 12));
    this.endDate = this.startDate.plusMonths(6);
    return this;
  }

  public AcademicTerm build() {
    return new AcademicTerm(startDate, endDate, year);
  }
}
