package com.udla.markenx.classroom.academicterms.application.commands;

import java.time.LocalDate;

public record CreateCommand(
		LocalDate startOfTerm,
		LocalDate endOfTerm,
		Integer academicYear,
		String createdBy) {
}
