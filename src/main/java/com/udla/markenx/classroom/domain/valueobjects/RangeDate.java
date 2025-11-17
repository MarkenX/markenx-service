package com.udla.markenx.classroom.domain.valueobjects;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.udla.markenx.classroom.domain.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.domain.exceptions.NullFieldException;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class RangeDate {
  private final LocalDate startDate;
  private final LocalDate endDate;

  public RangeDate(LocalDate startDate, LocalDate endDate) {
    validateNotNull(startDate, endDate);
    validateCoherence(startDate, endDate);

    this.startDate = startDate;
    this.endDate = endDate;
  }

  private void validateNotNull(LocalDate startDate, LocalDate endDate) {
    if (startDate == null) {
      throw new NullFieldException(getClass(), "startDate");
    }

    if (endDate == null) {
      throw new NullFieldException(getClass(), "endDate");
    }
  }

  private void validateCoherence(LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(endDate)) {
      throw new InvalidEntityException(getClass(), "dateRange",
          String.format("fecha de inicio (%s) no puede ser posterior a fecha de fin (%s)",
              startDate, endDate));
    }
  }

  public long getDurationInDays() {
    return ChronoUnit.DAYS.between(startDate, endDate);
  }

  public boolean contains(LocalDate date) {
    if (date == null) {
      return false;
    }
    return !date.isBefore(startDate) && !date.isAfter(endDate);
  }

  public boolean overlaps(RangeDate other) {
    if (other == null) {
      return false;
    }
    return !this.endDate.isBefore(other.startDate) &&
        !other.endDate.isBefore(this.startDate);
  }

  public boolean isActive() {
    LocalDate today = LocalDate.now();
    return contains(today);
  }

  public boolean isFuture() {
    return startDate.isAfter(LocalDate.now());
  }

  public boolean isPast() {
    return endDate.isBefore(LocalDate.now());
  }
}
