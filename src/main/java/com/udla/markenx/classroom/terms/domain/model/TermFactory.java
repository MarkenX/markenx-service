package com.udla.markenx.classroom.terms.domain.model;

import com.udla.markenx.shared.domain.valueobjects.AuditParams;
import com.udla.markenx.shared.domain.valueobjects.DateInterval;
import com.udla.markenx.classroom.terms.domain.utils.TermValidator;
import com.udla.markenx.classroom.terms.domain.valueobjects.TermStatus;
import com.udla.markenx.shared.domain.valueobjects.EntityInfo;

public class TermFactory {

    private static void validateSingleYearStructure(DateInterval interval, int sequence) {
        TermValidator.validateSingleYear(interval);
        TermValidator.validateMonthLength(interval);
        TermValidator.validateSequence(sequence);
    }

    private static void validateCrossYearStructure(DateInterval interval, int year, int sequence) {
        TermValidator.validateCrossYears(interval);
        TermValidator.validateCrossYearMonths(interval);
        TermValidator.validateMonthLength(interval);
        TermValidator.validateYear(year);
        TermValidator.validateSequence(sequence);
    }

    private static void validateCreationRules(DateInterval interval) {
        TermValidator.validateStartDateInFuture(interval);
        TermValidator.validateEndDateNotTooFar(interval);
    }

    private static void validateSingleYearCreation(DateInterval interval, int sequence) {
        validateSingleYearStructure(interval, sequence);
        validateCreationRules(interval);
    }

    private static void validateCrossYearCreation(DateInterval interval, int year, int sequence) {
        validateCrossYearStructure(interval, year, sequence);
        validateCreationRules(interval);
    }

    public static Term createSingleYearNew(DateInterval interval, int sequence, String createdBy) {
        validateSingleYearCreation(interval, sequence);
        return new Term(interval, interval.getEndYear(), sequence, TermStatus.UPCOMING, createdBy);
    }

    public static Term createCrossYearNew(DateInterval interval, int year, int sequence, String createdBy) {
        validateCrossYearCreation(interval, year, sequence);
        return new Term(interval, year, sequence, TermStatus.UPCOMING, createdBy);
    }

    public static Term createSingleYearHistorical(DateInterval interval, int sequence) {
        validateSingleYearStructure(interval, sequence);
        return new Term(interval, interval.getEndYear(), sequence, TermStatus.UPCOMING);
    }

    public static Term createCrossYearHistorical(DateInterval interval, int year, int sequence) {
        validateCrossYearStructure(interval, year, sequence);
        return new Term(interval, year, sequence, TermStatus.UPCOMING);
    }

    public static Term restoreSingleYearFromRepository(EntityInfo info, DateInterval interval, int sequence, AuditParams audit) {
        validateSingleYearStructure(interval, sequence);  // solo estructura
        return new Term(info, interval, interval.getEndYear(), sequence, TermStatus.UPCOMING, audit);
    }

    public static Term restoreCrossYearFromRepository(EntityInfo info, DateInterval interval, int year, int sequence, AuditParams audit) {
        validateCrossYearStructure(interval, year, sequence); // solo estructura
        return new Term(info, interval, year, sequence, TermStatus.UPCOMING, audit);
    }
}
