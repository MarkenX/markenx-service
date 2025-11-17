package com.udla.markenx.classroom.application.builders;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.core.models.AcademicTerm;

@Component
public class AcademicTermBuilder {

  private LocalDate startOfTerm;
  private LocalDate endOfTerm;
  private int academicYear;

  public AcademicTermBuilder reset() {
    this.startOfTerm = null;
    this.endOfTerm = null;
    this.academicYear = -1;
    return this;
  }

  public AcademicTermBuilder setStartOfTerm(LocalDate startDate) {
    this.startOfTerm = startDate;
    return this;
  }

  public AcademicTermBuilder setEndOfTerm(LocalDate endDate) {
    this.endOfTerm = endDate;
    return this;
  }

  public AcademicTermBuilder setAcademicYear(int year) {
    this.academicYear = year;
    return this;
  }

  public AcademicTermBuilder randomStartOfTerm() {
    this.startOfTerm = LocalDate.now().plusMonths((long) (Math.random() * 12));
    return this;
  }

  public AcademicTermBuilder randomEndOfTerm() {
    if (startOfTerm != null) {
      this.endOfTerm = this.startOfTerm.plusMonths(6);
    }
    return this;
  }

  public AcademicTerm build() {
    return new AcademicTerm(startOfTerm, endOfTerm, academicYear);
  }
}
