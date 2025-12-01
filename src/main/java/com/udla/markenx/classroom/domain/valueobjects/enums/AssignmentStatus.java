package com.udla.markenx.classroom.domain.valueobjects.enums;

public enum AssignmentStatus {
	NOT_STARTED("Sin empezar"),
	IN_PROGRESS("En curso"),
	COMPLETED("Completada"),
	OUTDATED("Vencida"),
	FAILED("Fallida");

	private final String label;

	AssignmentStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
