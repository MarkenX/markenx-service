package com.udla.markenx.classroom.terms.domain.utils;

import com.udla.markenx.shared.domain.utils.DateUtils;
import com.udla.markenx.shared.domain.valueobjects.DateInterval;

import java.time.LocalDate;

public class TermValidator {

    private static final int MIN_MONTHS_LENGTH = 4;
    private static final int MAX_MONTHS_LENGTH = 6;
    private static final int MIN_MONTHS_PER_YEAR = 2;

    private TermValidator() {}

    public static void validateYear(int year) {
        var nextYear = LocalDate.now().getYear() + 1;
        if (year < 1 || year > nextYear) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
    }

    public static void validateSequence(int sequence) {
        if (sequence <= 0) {
            throw new IllegalArgumentException("The sequence number must be positive");
        }
    }

    public static void validateSingleYear(DateInterval interval) {
        if (interval.spansMultipleYears()) {
            throw new IllegalArgumentException("The date interval must be within one year");
        }
    }

    public static void validateCrossYears(DateInterval interval) {
        if (interval.spansOneYear()) {
            throw new IllegalArgumentException("The date interval must be within two years");
        }
    }

    public static void validateStartDateInFuture(DateInterval interval) {
        if (LocalDate.now().plusDays(1).isBefore(interval.startDate())) {
            throw new IllegalArgumentException("The term must start in the future");
        }
    }

    public static void validateEndDateNotTooFar(DateInterval interval) {
        if (interval.getEndYear() > LocalDate.now().getYear() + 1) {
            throw new IllegalArgumentException("A term can only be created up to one year in the future");
        }
    }

    public static void validateMonthLength(DateInterval interval) {
        long monthLength = interval.getMonthLength();

        if (monthLength < MIN_MONTHS_LENGTH || monthLength > MAX_MONTHS_LENGTH) {
            throw new IllegalArgumentException("Invalid month length: " + monthLength);
        }
    }

    public static void validateCrossYearMonths(DateInterval interval) {
        long monthsAtStart = DateUtils.monthsToEndOfYear(interval.startDate());
        long monthsAtEnd = DateUtils.monthsFromStartOfYear(interval.endDate());

        if (monthsAtStart < MIN_MONTHS_PER_YEAR) {
            throw new IllegalArgumentException("Start date must be at least "
                    + MIN_MONTHS_PER_YEAR + " months before end of year");
        }

        if (monthsAtEnd < MIN_MONTHS_PER_YEAR) {
            throw new IllegalArgumentException("End date must be at least "
                    + MIN_MONTHS_PER_YEAR + " months after start of year");
        }
    }
}
