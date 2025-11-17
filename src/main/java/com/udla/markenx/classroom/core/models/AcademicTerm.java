package com.udla.markenx.classroom.core.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.udla.markenx.classroom.core.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.core.interfaces.DomainBaseModel;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;
import com.udla.markenx.classroom.core.valueobjects.enums.DomainBaseModelStatus;

public class AcademicTerm extends DomainBaseModel {
  private static final Class<AcademicTerm> CLAZZ = AcademicTerm.class;
  private static final String PREFIX = "AT";

  private final String code;
  private LocalDate startOfTerm;
  private LocalDate endOfTerm;
  private int academicYear;
  private int termNumber;
  private final List<Course> assignedCourses;

  public AcademicTerm(UUID id, String code, DomainBaseModelStatus status, LocalDate startOfTerm, LocalDate endOfTerm,
      int academicYear, List<Course> assignedCourses, String createdBy, LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super(id, code, status, createdBy, createdAt, updatedAt);
    this.startOfTerm = requireStartOfTerm(startOfTerm);
    this.endOfTerm = requireEndOfTerm(endOfTerm);
    validateChronology(this.startOfTerm, this.endOfTerm);
    this.academicYear = validateAcademicYear(academicYear);
    this.termNumber = determineTermNumber(this.startOfTerm);
    this.assignedCourses = requireAssignedCourses(assignedCourses);
    this.code = requireCode(code);
  }

  public AcademicTerm(LocalDate startOfTerm, LocalDate endOfTerm, int academicYear, String createdBy) {
    super(createdBy);
    this.startOfTerm = requireStartOfTerm(startOfTerm);
    this.endOfTerm = requireEndOfTerm(endOfTerm);
    validateChronology(this.startOfTerm, this.endOfTerm);
    this.academicYear = validateAcademicYear(academicYear);
    this.termNumber = determineTermNumber(this.startOfTerm);
    this.assignedCourses = new ArrayList<>();
    this.code = generateCode();
  }

  public AcademicTerm(LocalDate startOfTerm, LocalDate endOfTerm, int academicYear) {
    super();
    this.startOfTerm = requireStartOfTerm(startOfTerm);
    this.endOfTerm = requireEndOfTerm(endOfTerm);
    validateChronology(this.startOfTerm, this.endOfTerm);
    this.academicYear = validateAcademicYear(academicYear);
    this.termNumber = determineTermNumber(this.startOfTerm);
    this.assignedCourses = new ArrayList<>();
    this.code = generateCode();
  }

  public String getCode() {
    return this.code;
  }

  public LocalDate getStartOfTerm() {
    return this.startOfTerm;
  }

  public LocalDate getEndOfTerm() {
    return this.endOfTerm;
  }

  public int getAcademicYear() {
    return this.academicYear;
  }

  public int getTermNumber() {
    return this.termNumber;
  }

  public List<Course> getAssignedCourses() {
    return List.copyOf(this.assignedCourses);
  }

  public String getLabel() {
    String semesterLabel = this.termNumber == 1 ? "1er Semestre" : "2do Semestre";
    return String.format("%s - %d", semesterLabel, this.academicYear);
  }

  public void setStartOfTerm(LocalDate startDate) {
    LocalDate validated = requireStartOfTerm(startDate);
    validateChronology(validated, this.endOfTerm);
    this.startOfTerm = validated;
    updateTermNumber();
  }

  public void setEndOfTerm(LocalDate endDate) {
    LocalDate validated = requireEndOfTerm(endDate);
    validateChronology(this.startOfTerm, validated);
    this.endOfTerm = endDate;
  }

  public void setAcademicYear(int year) {
    this.academicYear = validateAcademicYear(year);
  }

  private LocalDate requireStartOfTerm(LocalDate startOfTerm) {
    return EntityValidator.ensureNotNull(CLAZZ, startOfTerm, "startOfTerm");
  }

  private LocalDate requireEndOfTerm(LocalDate endOfTerm) {
    return EntityValidator.ensureNotNull(CLAZZ, endOfTerm, "endOfTerm");
  }

  private void validateChronology(LocalDate start, LocalDate end) {
    if (end.isBefore(start)) {
      throw new InvalidEntityException(CLAZZ, "debe ser la fecha actual.");
    }
  }

  private List<Course> requireAssignedCourses(List<Course> courses) {
    return List.copyOf(EntityValidator.ensureNotNull(CLAZZ, assignedCourses, "assignedCourses"));
  }

  private int validateAcademicYear(int academicYear) {
    int currentYear = LocalDate.now().getYear();
    int nextYear = currentYear + 1;

    if (academicYear < currentYear || academicYear > nextYear) {
      throw new InvalidEntityException(CLAZZ,
          String.format("El año debe ser el año actual (%d) o el siguiente (%d)", currentYear, nextYear));
    }

    int startYear = this.startOfTerm.getYear();
    int endYear = this.endOfTerm.getYear();

    if (academicYear != startYear && academicYear != endYear) {
      throw new InvalidEntityException(CLAZZ,
          String.format("El año %d no corresponde con las fechas del período (inicia en %d, termina en %d)",
              academicYear, startYear, endYear));
    }

    return academicYear;
  }

  private int determineTermNumber(LocalDate startOfTerm) {
    int month = startOfTerm.getMonthValue();
    return (month <= 6) ? 1 : 2;
  }

  public void updateTermNumber() {
    this.termNumber = determineTermNumber(this.startOfTerm);
  }

  public boolean overlapsWith(AcademicTerm other) {
    if (other == null) {
      return false;
    }
    return !this.startOfTerm.isAfter(other.endOfTerm) && !this.endOfTerm.isBefore(other.startOfTerm);
  }

  public boolean addCourse(Course course) {
    if (course == null) {
      return false;
    }
    return this.assignedCourses.add(course);
  }

  @Override
  protected String generateCode() {
    int term = determineTermNumber(startOfTerm);
    return String.format("%s-%d-%02d", PREFIX, academicYear, term);
  }
}
