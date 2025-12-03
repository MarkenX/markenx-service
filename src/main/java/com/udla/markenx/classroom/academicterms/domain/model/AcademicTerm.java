package com.udla.markenx.classroom.academicterms.domain.model;

import java.time.LocalDate;

import com.udla.markenx.shared.domain.util.EntityValidator;
import com.udla.markenx.shared.domain.model.DomainBaseModel;
import com.udla.markenx.shared.domain.valueobjects.AuditParams;
import com.udla.markenx.shared.domain.valueobjects.EntityInfo;

import com.udla.markenx.classroom.academicterms.domain.valueobjects.AcademicTermStatus;

public class AcademicTerm extends DomainBaseModel {

  private static final int MIN_MONTHS_AHEAD = 4;

  private final AcademicTermStatus termStatus;
  private final LocalDate termStartDate;
  private final int termNumber;

  private LocalDate termEndDate;
  private int academicYear;

  // Para cuando se crea un nuevo periodo académico desde el servicio
  public AcademicTerm(
      LocalDate termStartDate,
      LocalDate termEndDate,
      int academicYear,
      int termNumber) {

    super();
    this.termStartDate = EntityValidator.ensureNotNull(
        getClass(), termStartDate, "termStartDate");
    this.termEndDate = EntityValidator.ensureNotNull(
        getClass(), termEndDate, "termEndDate");

    this.academicYear = validateAcademicYear(
        academicYear, this.termStartDate, this.termEndDate);

    // Calcular term number? (Puede haber 2 a 3 periodos académicos en un año) / En
    // el servicio
    this.termNumber = EntityValidator.ensureNotNull(
        getClass(), termNumber, "termNumber");

    this.termStatus = calculateStatus(this.termStartDate, this.termEndDate);
  }

  // Para cuando se crea un nuevo periodo académico con datos fuera del servicio
  public AcademicTerm(
      LocalDate termStartDate,
      LocalDate termEndDate,
      int academicYear,
      int termNumber,
      String createdBy) {

    super(createdBy);

    validateTerm(termStartDate, termEndDate);
    this.termStartDate = termStartDate;
    this.termEndDate = termEndDate;

    this.academicYear = validateAcademicYear(
        academicYear, this.termStartDate, this.termEndDate);

    // Calcular term number? (Puede haber 2 a 3 periodos académicos en un año) / En
    // el servicio
    this.termNumber = EntityValidator.ensureNotNull(
        getClass(), termNumber, "termNumber");

    // Definir el term status en función de las fechas de inicio y final
    this.termStatus = calculateStatus(this.termStartDate, this.termEndDate);
  }

  // Para cuando se carga un periodo académico desde la base de datos
  public AcademicTerm(
      EntityInfo entityInfo,
      LocalDate termStartDate,
      LocalDate termEndDate,
      int academicYear,
      int termNumber,
      AuditParams auditParams) {

    super(entityInfo.id(), entityInfo.status(), auditParams);

    validateTerm(termStartDate, termEndDate);
    this.termStartDate = termStartDate;
    this.termEndDate = termEndDate;

    // Validar academic year (Que el año esté dentro del periodo)
    this.academicYear = validateAcademicYear(
        academicYear, this.termStartDate, this.termEndDate);

    // Calcular term number? (Puede haber 2 a 3 periodos académicos en un año)
    this.termNumber = EntityValidator.ensureNotNull(
        getClass(), termNumber, "termNumber");

    // Definir el term status en función de las fechas de inicio y final
    this.termStatus = calculateStatus(this.termStartDate, this.termEndDate);
  }

  // #region Getters

  public LocalDate getTermStartDate() {
    return termStartDate;
  }

  public LocalDate getTermEndDate() {
    return termEndDate;
  }

  public int getAcademicYear() {
    return academicYear;
  }

  public int getTermNumber() {
    return termNumber;
  }

  public AcademicTermStatus getTermStatus() {
    return this.termStatus;
  }

  // #endregion

  // #region Validations

  private int validateAcademicYear(int academicYear, LocalDate termStartDate, LocalDate termEndDate) {
    if (academicYear >= getStartOfTermYear() && academicYear <= getEndOfTermYear()) {
      return academicYear;
    }
    return -1; // Deberiamos lanzar una excepción
  }

  private void validateTerm(LocalDate termStartDate, LocalDate termEndDate) {
    validateNotNull(termStartDate, termEndDate);
    validateStartBeforeEnd(termStartDate, termEndDate);
    validateNotInPast(termEndDate);
    validateMinStartOffset(termStartDate, MIN_MONTHS_AHEAD);
  }

  private void validateNotNull(LocalDate termStartDate, LocalDate termEndDate) {
    if (termStartDate == null || termEndDate == null) {
      throw new IllegalArgumentException("Start and end dates cannot be null.");
    }
  }

  private void validateStartBeforeEnd(LocalDate termStartDate, LocalDate termEndDate) {
    if (!termStartDate.isBefore(termEndDate)) {
      throw new IllegalArgumentException("Start date must be before end date.");
    }
  }

  private void validateNotInPast(LocalDate termEndDate) {
    if (termEndDate.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("The academic term cannot be entirely in the past.");
    }
  }

  private void validateMinStartOffset(LocalDate termStartDate, int monthsAhead) {
    LocalDate minAllowedStart = LocalDate.now().plusMonths(monthsAhead);

    if (termStartDate.isBefore(minAllowedStart)) {
      throw new IllegalArgumentException(
          "The academic term must start at least " + monthsAhead + " months in the future.");
    }
  }

  // #endregion

  private int getStartOfTermYear() {
    if (termStartDate == null)
      return -1;
    return this.termStartDate.getYear();
  }

  private int getEndOfTermYear() {
    if (termEndDate == null)
      return -1;
    return this.termEndDate.getYear();
  }

  private AcademicTermStatus calculateStatus(LocalDate termStartDate, LocalDate termEndDate) {
    LocalDate today = LocalDate.now();
    if (today.isAfter(termStartDate) && today.isBefore(termEndDate)) {
      return AcademicTermStatus.ACTIVE;
    }
    if (today.isBefore(termStartDate)) {
      return AcademicTermStatus.UPCOMING;
    }
    return AcademicTermStatus.ENDED;
  }
}
