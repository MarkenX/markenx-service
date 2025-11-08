package com.udla.markenx.application.builders;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.core.models.AcademicPeriod;

@Component
public class AcademicPeriodBuilder {

  private LocalDate startDate;
  private LocalDate endDate;
  private int year;
  private int semesterNumber;

  public AcademicPeriodBuilder reset() {
    this.startDate = null;
    this.endDate = null;
    this.year = 0;
    this.semesterNumber = 0;
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

  public AcademicPeriodBuilder setYear(int year) {
    this.year = year;
    return this;
  }

  public AcademicPeriodBuilder setSemesterNumber(int semesterNumber) {
    this.semesterNumber = semesterNumber;
    return this;
  }

  public AcademicPeriodBuilder randomDates() {
    this.startDate = LocalDate.now().plusMonths((long) (Math.random() * 12));
    this.endDate = this.startDate.plusMonths(6);
    return this;
  }

  public AcademicPeriod build() {
    return new AcademicPeriod(startDate, endDate, year, semesterNumber);
  }
}
