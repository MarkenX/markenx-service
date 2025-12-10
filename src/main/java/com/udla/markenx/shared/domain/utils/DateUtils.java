package com.udla.markenx.shared.domain.utils;

import com.udla.markenx.shared.domain.valueobjects.DateInterval;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class DateUtils {

    private DateUtils() {
        // Evitar instanciación
    }

    /**
     * Calcula cuántos meses completos hay entre la fecha dada y el fin del año.
     * @param date fecha a evaluar
     * @return meses completos hasta el fin del año
     */
    public static long monthsToEndOfYear(LocalDate date) {
        LocalDate endOfYear = LocalDate.of(date.getYear(), 12, 31);
        return ChronoUnit.MONTHS.between(
                date.withDayOfMonth(1),
                endOfYear.withDayOfMonth(1)
        );
    }

    /**
     * Calcula cuántos meses completos han pasado desde el inicio del año hasta la fecha dada.
     * @param date fecha a evaluar
     * @return meses completos desde inicio del año
     */
    public static long monthsFromStartOfYear(LocalDate date) {
        LocalDate startOfYear = LocalDate.of(date.getYear(), 1, 1);
        return ChronoUnit.MONTHS.between(
                startOfYear.withDayOfMonth(1),
                date.withDayOfMonth(1)
        );
    }

    public static void validateCrossYearIntervalMonths(
            DateInterval interval,
            int minMonthsPerYear
    ) {
        long monthsToEnd = monthsToEndOfYear(interval.startDate());
        long monthsFromStart = monthsFromStartOfYear(interval.endDate());

        if (monthsToEnd < minMonthsPerYear) {
            throw new IllegalArgumentException(
                    "The interval does not have the minimum required months (" +
                            minMonthsPerYear + ") before the end of the year. Found: " + monthsToEnd
            );
        }

        if (monthsFromStart < minMonthsPerYear) {
            throw new IllegalArgumentException(
                    "The interval does not have the minimum required months (" +
                            minMonthsPerYear + ") after the start of the new year. Found: " + monthsFromStart
            );
        }
    }

}