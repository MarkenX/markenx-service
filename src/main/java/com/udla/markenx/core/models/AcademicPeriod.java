package com.udla.markenx.core.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.exceptions.NullFieldException;

import lombok.Getter;

@Getter
public class AcademicPeriod {
  private final Long id;
  private LocalDate startDate;
  private LocalDate endDate;
  private String label;
  private final List<Course> courses;

  // #region Constructors

  public AcademicPeriod(LocalDate startDate, LocalDate endDate, String label) {
    this.id = null;
    this.startDate = ensureValidStartDate(startDate);
    this.endDate = ensureValidEndDate(endDate);
    ensureValidPeriod(startDate, endDate);
    this.label = ensureValidLabel(label);
    this.courses = new ArrayList<>();
  }

  public AcademicPeriod(Long id, LocalDate startDate, LocalDate endDate, String label,
      List<Course> courses) {
    this.id = ensureValidId(id);
    this.startDate = ensureValidStartDate(startDate);
    this.endDate = ensureValidEndDate(endDate);
    ensureValidPeriod(startDate, endDate);
    this.label = ensureValidLabel(label);
    this.courses = ensureValidCourses(courses);
  }

  // #endregion Constructors

  // #region Setters

  public void setStartDate(LocalDate startDate) {
    LocalDate validated = ensureValidStartDate(startDate);
    ensureValidPeriod(validated, this.endDate);
    this.startDate = validated;
  }

  public void setEndDate(LocalDate endDate) {
    LocalDate validated = ensureValidEndDate(endDate);
    ensureValidPeriod(this.startDate, validated);
    this.endDate = endDate;
  }

  public void setLabel(String label) {
    this.label = ensureValidLabel(label);
  }

  // #endregion

  // #region Validations

  private long ensureValidId(Long id) {
    if (id == null) {
      throw new NullFieldException("AcademicPeriod", "id");
    }
    return id;
  }

  private LocalDate ensureValidStartDate(LocalDate startDate) {
    if (startDate == null) {
      throw new NullFieldException("AcademicPeriod", "startDate");
    }
    return startDate;
  }

  private LocalDate ensureValidEndDate(LocalDate endDate) {
    if (endDate == null) {
      throw new NullFieldException("AcademicPeriod", "endDate");
    }
    return endDate;
  }

  private void ensureValidPeriod(LocalDate start, LocalDate end) {
    if (end.isBefore(start)) {
      throw new InvalidEntityException("AcademicPeriod", "debe ser la fecha actual.");
    }
  }

  private String ensureValidLabel(String label) {
    if (label == null) {
      throw new NullFieldException("AcademicPeriod", "label");
    }
    return label;
  }

  private List<Course> ensureValidCourses(List<Course> courses) {
    if (courses == null) {
      throw new NullFieldException("AcademicPeriod", "label");
    }
    return courses;
  }

  // #endregion Validations

  public boolean addCourse(Course course) {
    if (course == null) {
      return false;
    }
    return this.courses.add(course);
  }
}
