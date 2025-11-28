package com.udla.markenx.classroom.domain.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.udla.markenx.shared.domain.model.DomainBaseModel;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

public class AcademicTerm extends DomainBaseModel {

  private static final String PREFIX = "AT";

  private LocalDate startOfTerm;
  private LocalDate endOfTerm;
  private int academicYear;
  private int termNumber;

  private final List<Course> assignedCourses;

  // Rehidradatación BD [No eliminar]
  public AcademicTerm(
      UUID id,
      String code,
      DomainBaseModelStatus status,
      LocalDate startOfTerm,
      LocalDate endOfTerm,
      int academicYear,
      int termNumber,
      String createdBy,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {

    super(id, code, status, createdBy, createdAt, updatedAt);

    this.startOfTerm = EntityValidator.ensureNotNull(AcademicTerm.class, startOfTerm, "startOfTerm");
    this.endOfTerm = EntityValidator.ensureNotNull(AcademicTerm.class, endOfTerm, "endOfTerm");

    this.academicYear = academicYear;
    this.termNumber = termNumber;
    this.assignedCourses = new ArrayList<>();
  }

  // Para crear nuevo periodo académico
  public AcademicTerm(
      UUID id,
      LocalDate startOfTerm,
      LocalDate endOfTerm,
      int academicYear,
      int termNumber,
      String createdBy) {

    super(createdBy);

    this.startOfTerm = EntityValidator.ensureNotNull(AcademicTerm.class, startOfTerm, "startOfTerm");
    this.endOfTerm = EntityValidator.ensureNotNull(AcademicTerm.class, endOfTerm, "endOfTerm");

    this.academicYear = academicYear;
    this.termNumber = termNumber;
    this.assignedCourses = new ArrayList<>();
  }

  public static AcademicTerm createNew(
      LocalDate startOfTerm,
      LocalDate endOfTerm,
      int academicYear,
      int termNumber,
      String createdBy) {
    return new AcademicTerm(
        UUID.randomUUID(),
        startOfTerm,
        endOfTerm,
        academicYear,
        termNumber,
        createdBy);
  }

  public LocalDate getStartOfTerm() {
    return startOfTerm;
  }

  public LocalDate getEndOfTerm() {
    return endOfTerm;
  }

  public int getAcademicYear() {
    return academicYear;
  }

  public int getTermNumber() {
    return termNumber;
  }

  public List<Course> getAssignedCourses() {
    return List.copyOf(assignedCourses);
  }

  public void applyUpdate(LocalDate start, LocalDate end, int year, int termNumber) {
    EntityValidator.ensureNotNull(AcademicTerm.class, start, "startOfTerm");
    EntityValidator.ensureNotNull(AcademicTerm.class, end, "endOfTerm");

    this.startOfTerm = start;
    this.endOfTerm = end;
    this.academicYear = year;
    this.termNumber = termNumber;

    regenerateCode();
    markUpdated();
  }

  public String getLabel() {
    return String.format("%s Semestre - %d",
        termNumber == 1 ? "1er" : "2do", academicYear);
  }

  public boolean overlapsWith(AcademicTerm other) {
    if (other == null)
      return false;
    return !this.startOfTerm.isAfter(other.endOfTerm)
        && !this.endOfTerm.isBefore(other.startOfTerm);
  }

  public boolean addCourse(Course course) {
    if (course == null)
      return false;
    return assignedCourses.add(course);
  }

  @Override
  protected String generateCode() {
    return String.format("%s-%d-%02d", PREFIX, academicYear, termNumber);
  }
}
