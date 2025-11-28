package com.udla.markenx.classroom.application.commands;

import java.time.LocalDate;

public record CreateAcademicTermCommand(
		LocalDate startOfTerm,
		LocalDate endOfTerm,
		Integer academicYear,
		String createdBy) {
}
