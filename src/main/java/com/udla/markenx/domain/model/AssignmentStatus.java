package com.udla.markenx.domain.model;

public enum AssignmentStatus {
    NOT_STARTED("Sin empezar"),
    IN_PROGRESS("En curso"),
    COMPLETED("Completada"),
    OUTDATED("Vencida");

    private final String label;

    AssignmentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
