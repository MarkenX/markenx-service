package com.udla.markenx.core.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.exceptions.NullFieldException;
import com.udla.markenx.core.utils.validators.EntityValidator;

import lombok.Getter;

@Getter
public class AcademicPeriod {
  private final Long id;
  private LocalDate startDate;
  private LocalDate endDate;
  private String label;
  private int year;
  private final int semesterNumber;
  private final List<Course> courses;

  // #region Constructors

  /**
   * Constructor for new academic periods.
   * The semester number will be determined by the service layer based on existing
   * periods.
   */
  public AcademicPeriod(LocalDate startDate, LocalDate endDate, int year, int semesterNumber) {
    this.id = null;
    this.startDate = ensureValidStartDate(startDate);
    this.endDate = ensureValidEndDate(endDate);
    ensureValidPeriod(startDate, endDate);
    this.year = ensureValidYear(year);
    this.semesterNumber = ensureValidSemesterNumber(semesterNumber);
    this.label = generateLabel(semesterNumber, year);
    this.courses = new ArrayList<>();
  }

  /**
   * Constructor for existing academic periods.
   */
  public AcademicPeriod(Long id, LocalDate startDate, LocalDate endDate, int year,
      int semesterNumber, List<Course> courses) {
    this.id = EntityValidator.ensureValidId(getClass(), id);
    this.startDate = ensureValidStartDate(startDate);
    this.endDate = ensureValidEndDate(endDate);
    ensureValidPeriod(startDate, endDate);
    this.year = ensureValidYear(year);
    this.semesterNumber = ensureValidSemesterNumber(semesterNumber);
    this.label = generateLabel(semesterNumber, year);
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

  public void setYear(int year) {
    this.year = ensureValidYear(year);
    // Regenerate label when year changes
    this.label = generateLabel(this.semesterNumber, this.year);
  }

  // #endregion

  // #region Validations

  private LocalDate ensureValidStartDate(LocalDate startDate) {
    if (startDate == null) {
      throw new NullFieldException(getClass(), "startDate");
    }
    return startDate;
  }

  private LocalDate ensureValidEndDate(LocalDate endDate) {
    if (endDate == null) {
      throw new NullFieldException(getClass(), "endDate");
    }
    return endDate;
  }

  private void ensureValidPeriod(LocalDate start, LocalDate end) {
    if (end.isBefore(start)) {
      throw new InvalidEntityException(getClass(), "debe ser la fecha actual.");
    }
  }

  private List<Course> ensureValidCourses(List<Course> courses) {
    if (courses == null) {
      throw new NullFieldException(getClass(), "courses");
    }
    return courses;
  }

  private int ensureValidYear(int year) {
    int currentYear = java.time.LocalDate.now().getYear();
    int nextYear = currentYear + 1;

    if (year < currentYear || year > nextYear) {
      throw new InvalidEntityException(getClass(),
          String.format("El año debe ser el año actual (%d) o el siguiente (%d)", currentYear, nextYear));
    }

    // Validate that the year matches the period dates
    int startYear = this.startDate.getYear();
    int endYear = this.endDate.getYear();

    if (year != startYear && year != endYear) {
      throw new InvalidEntityException(getClass(),
          String.format("El año %d no corresponde con las fechas del período (inicia en %d, termina en %d)",
              year, startYear, endYear));
    }

    return year;
  }

  private int ensureValidSemesterNumber(int semesterNumber) {
    if (semesterNumber < 1 || semesterNumber > 2) {
      throw new InvalidEntityException(getClass(), "El número de semestre debe ser 1 o 2");
    }
    return semesterNumber;
  }

  private String generateLabel(int semesterNumber, int year) {
    String semesterLabel = semesterNumber == 1 ? "1er Semestre" : "2do Semestre";
    return String.format("%s - %d", semesterLabel, year);
  }

  // #endregion Validations

  // #region Business Logic

  public boolean overlapsWith(AcademicPeriod other) {
    if (other == null) {
      return false;
    }
    return !this.startDate.isAfter(other.endDate) && !this.endDate.isBefore(other.startDate);
  }

  public boolean addCourse(Course course) {
    if (course == null) {
      return false;
    }
    return this.courses.add(course);
  }

  // #endregion Business Logic
}
