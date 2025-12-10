package com.udla.markenx.shared.domain.valueobjects;

import com.udla.markenx.shared.domain.exceptions.NullFieldException;
import com.udla.markenx.classroom.terms.domain.model.Term;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record DateInterval(LocalDate startDate, LocalDate endDate) {

    private static final Class<Term> CLAZZ = Term.class;

    public DateInterval(LocalDate startDate, LocalDate endDate) {
        this.startDate = validateStartDate(startDate);
        this.endDate = validateEndDate(endDate);
        validateInterval(this.startDate, this.endDate);
    }

    private @NotNull LocalDate validateStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new NullFieldException(CLAZZ, "startDate");
        }
        return startDate;
    }

    private @NotNull LocalDate validateEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new NullFieldException(CLAZZ, "endDate");
        }
        return endDate;
    }

    private void validateInterval(LocalDate startDate, @NotNull LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    public int getEndYear() {
        return endDate.getYear();
    }

    public boolean spansOneYear() {
        int startYear = startDate.getYear();
        int endYear = endDate.getYear();
        return startYear == endYear;
    }

    public boolean spansMultipleYears() {
        return !spansOneYear();
    }

    public long getMonthLength() {
        return ChronoUnit.MONTHS.between(startDate, endDate);
    }

    public boolean overlapsWith(@NotNull DateInterval other) {
        return this.equals(other) || contains(other.startDate) || contains(other.endDate);
    }

    public boolean contains(@NotNull LocalDate date) {
        return date.isAfter(startDate) && date.isBefore(endDate);
    }
}
