package com.udla.markenx.classroom.application.ports.out.persistance.repositories;

import org.springframework.data.domain.Page;

import com.udla.markenx.classroom.core.models.Attempt;

public interface AttemptRepositoryPort {
    Page<Attempt> getAttemptsByStudentAssignmentId(Long studentAssignmentId, int page, int size);

    Page<Attempt> getAttemptsByTaskId(Long taskId, int page, int size);

    /**
     * Persist a new attempt for the provided taskId and studentId. Returns the
     * persisted Attempt.
     * Implementations must not allow updates to existing attempts (no update/delete
     * semantics).
     */
    Attempt createAttempt(Attempt attempt, Long studentAssignmentId);
}
