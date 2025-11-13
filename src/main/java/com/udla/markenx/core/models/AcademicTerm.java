package com.udla.markenx.core.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.utils.validators.EntityValidator;
import com.udla.markenx.core.valueobjects.AuditInfo;

public class AcademicTerm {
  private static final Class<AcademicTerm> CLAZZ = AcademicTerm.class;

  private final Long id;
  private LocalDate startOfTerm;
  private LocalDate endOfTerm;
  private int academicYear;
  private int termNumber;
  private final List<Course> assignedCourses;
  private final AuditInfo auditInfo;

  private AcademicTerm(Long id, LocalDate startOfTerm, LocalDate endOfTerm, int academicYear,
      List<Course> assignedCourses, AuditInfo auditInfo) {
    this.id = id;
    this.startOfTerm = requireStartOfTerm(startOfTerm);
    this.endOfTerm = requireEndOfTerm(endOfTerm);
    validateChronology(this.startOfTerm, this.endOfTerm);
    this.academicYear = validateAcademicYear(academicYear);
    this.termNumber = determineTermNumber(this.startOfTerm);
    this.assignedCourses = requireAssignedCourses(assignedCourses);
    this.auditInfo = requireAuditInfo(auditInfo);
  }

  public AcademicTerm(Long id, LocalDate startOfTerm, LocalDate endOfTerm, int academicYear,
      List<Course> assignedCourses, String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this(id, startOfTerm, endOfTerm, academicYear, assignedCourses, new AuditInfo(createdBy, createdAt, updatedAt));
  }

  public AcademicTerm(LocalDate startOfTerm, LocalDate endOfTerm, int academicYear) {
    this(null, startOfTerm, endOfTerm, academicYear, new ArrayList<>(), new AuditInfo());
  }

  public Long getId() {
    return this.id;
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

  public List<Course> getAssignedCourses() {
    return List.copyOf(this.assignedCourses);
  }

  public String getLabel() {
    String semesterLabel = this.termNumber == 1 ? "1er Semestre" : "2do Semestre";
    return String.format("%s - %d", semesterLabel, this.academicYear);
  }

  public String getCreatedBy() {
    return this.auditInfo.getCreatedBy();
  }

  public LocalDateTime getCreatedAt() {
    return this.auditInfo.getCreatedDateTime();
  }

  public LocalDateTime getUpdatedAt() {
    return this.auditInfo.getUpdatedDateTime();
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

  private AuditInfo requireAuditInfo(AuditInfo auditInfo) {
    return EntityValidator.ensureNotNull(CLAZZ, auditInfo, "auditInfo");
  }

  public void markUpdated() {
    this.auditInfo.markUpdated();
  }
}
