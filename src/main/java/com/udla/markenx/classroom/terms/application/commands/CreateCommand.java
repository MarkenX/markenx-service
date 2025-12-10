package com.udla.markenx.classroom.terms.application.commands;

import java.time.LocalDate;

public record CreateCommand(
        LocalDate startDate,
        LocalDate endDate,
        Integer year,
        String createdBy,
        boolean isHistorical) {
}
