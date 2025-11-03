package com.udla.markenx.core.models;

import java.time.LocalDate;
import java.util.List;

import com.udla.markenx.core.enums.AssignmentStatus;
import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.interfaces.Assignment;

import lombok.Getter;
import lombok.EqualsAndHashCode;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Task extends Assignment {
    private static final int MIN_ATTEMPT = 0;

    private int activeAttempt;
    private int maxAttempts;
    private List<Attempt> attempts;
    private double minimumScoreToPass;

    // #region Constructors

    public Task(String title, String summary, LocalDate dueDate, int maxAttempts) {
        super(title, summary, dueDate);
        setMaxAttempts(maxAttempts);
        setActiveAttempt(MIN_ATTEMPT);
    }

    public Task(long id, String title, String summary, LocalDate dueDate, int maxAttempts) {
        super(id, title, summary, dueDate);
        setMaxAttempts(maxAttempts);
        setActiveAttempt(MIN_ATTEMPT);
    }

    public Task(long id, String title, String summary, LocalDate dueDate, int maxAttempts, int activeAttempt) {
        super(title, summary, dueDate);
        setMaxAttempts(maxAttempts);
        setActiveAttempt(activeAttempt);
    }

    // #endregion Constructors

    // #region Setters

    public void setMaxAttempts(int maxAttempts) {
        validateMaxAttempts(maxAttempts);
        this.maxAttempts = maxAttempts;
        if (this.activeAttempt > MIN_ATTEMPT) {
            validateActiveAttempt(this.activeAttempt);
        }
    }

    public void setActiveAttempt(int activeAttempt) {
        validateActiveAttempt(activeAttempt);
        this.activeAttempt = activeAttempt;
        updateStatus();
    }

    // #endregion Setters

    // #region Validations

    private void validateMaxAttempts(int maxAttempts) {
        if (maxAttempts <= MIN_ATTEMPT) {
            throw new InvalidEntityException("Task", "maxAttempts",
                    "debe ser mayor a 0.");
        }
    }

    private void validateActiveAttempt(int activeAttempt) {
        if (activeAttempt < MIN_ATTEMPT || isOverMaxAttempts()) {
            throw new InvalidEntityException("Task", "activeAttempt",
                    "debe ser mayor o igual a 0 y menor o igual a maxAttempts.");
        }
    }

    // #endregion Validations

    @Override
    public void updateStatus() {
        if (isOverdue()) {
            this.currentStatus = AssignmentStatus.OUTDATED;
            return;
        }
        if (isNotStarted()) {
            this.currentStatus = AssignmentStatus.NOT_STARTED;
            return;
        }
        if (didAnyAttemptPass()) {
            this.currentStatus = AssignmentStatus.COMPLETED;
            return;
        } else if (isOverMaxAttempts()) {
            this.currentStatus = AssignmentStatus.FAILED;
            return;
        }
        this.currentStatus = AssignmentStatus.IN_PROGRESS;
    }

    private boolean isOverdue() {
        return getDueDate().isBefore(LocalDate.now());
    }

    private boolean isNotStarted() {
        return this.activeAttempt == 0;
    }

    private boolean isOverMaxAttempts() {
        return this.activeAttempt > this.maxAttempts;
    }

    private boolean didAnyAttemptPass() {
        if (attempts == null || attempts.isEmpty()) {
            return false;
        }

        return attempts.stream().anyMatch(attempt -> attempt.getScore() >= minimumScoreToPass);
    }
}
