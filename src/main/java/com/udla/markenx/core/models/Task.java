package com.udla.markenx.core.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.interfaces.Assignment;
import com.udla.markenx.core.valueobjects.Score;
import com.udla.markenx.core.valueobjects.Timestamps;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

import lombok.Getter;
import lombok.EqualsAndHashCode;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Task extends Assignment {
	private static final int MIN_ATTEMPT = 0;

	private int activeAttempt;
	private int maxAttempts;
	private Score minimumScoreToPass;
	private final List<Attempt> attempts;
	private final Timestamps timestamps;

	// #region Constructors

	public Task(String title, String summary, LocalDate dueDate, int maxAttempts, double minimumScoreToPass) {
		super(title, summary, dueDate);
		this.maxAttempts = ensureValidMaxAttempts(maxAttempts);
		this.activeAttempt = ensureValidActiveAttempt(MIN_ATTEMPT);
		this.minimumScoreToPass = new Score(minimumScoreToPass);
		this.attempts = new ArrayList<>();
		this.timestamps = new Timestamps();
		updateStatus();
	}

	public Task(String title, String summary, LocalDate dueDate, int maxAttempts, int activeAttempt,
			double minimumScoreToPass, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(title, summary, dueDate);
		this.maxAttempts = ensureValidMaxAttempts(maxAttempts);
		this.activeAttempt = ensureValidActiveAttempt(activeAttempt);
		this.minimumScoreToPass = new Score(minimumScoreToPass);
		this.attempts = new ArrayList<>();
		this.timestamps = new Timestamps();
		updateStatus();
	}

	public Task(long id, String title, String summary, LocalDate dueDate, int maxAttempts, int activeAttempt,
			double minimumScoreToPass, List<Attempt> attempts, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(title, summary, dueDate);
		this.maxAttempts = ensureValidMaxAttempts(maxAttempts);
		this.activeAttempt = ensureValidActiveAttempt(activeAttempt);
		this.minimumScoreToPass = new Score(minimumScoreToPass);
		this.attempts = attempts;
		this.timestamps = new Timestamps(createdAt, updatedAt);
		updateStatus();
	}

	// #endregion Constructors

	// #region Getters

	public double getMinimumScoreToPass() {
		return this.minimumScoreToPass.value();
	}

	public LocalDate getCreatedDate() {
		return timestamps.getCreatedDate();
	}

	public LocalDateTime getCreatedDateTime() {
		return timestamps.getCreatedDateTime();
	}

	public LocalDate getUpdatedDate() {
		return timestamps.getUpdatedDate();
	}

	public LocalDateTime getUpdatedDateTime() {
		return timestamps.getUpdatedDateTime();
	}

	// #endregion Getters

	// #region Setters

	public void setMaxAttempts(int maxAttempts) {
		ensureValidMaxAttempts(maxAttempts);
		this.maxAttempts = maxAttempts;
		if (this.activeAttempt > MIN_ATTEMPT) {
			ensureValidActiveAttempt(this.activeAttempt);
		}
	}

	public void setActiveAttempt(int activeAttempt) {
		ensureValidActiveAttempt(activeAttempt);
		this.activeAttempt = activeAttempt;
		updateStatus();
	}

	public void setMinimumScoreToPass(Score minimumScoreToPass) {
		this.minimumScoreToPass = minimumScoreToPass;
		updateStatus();
	}

	// #endregion Setters

	// #region Validations

	private int ensureValidMaxAttempts(int maxAttempts) {
		if (maxAttempts <= MIN_ATTEMPT) {
			throw new InvalidEntityException("Task", "maxAttempts",
					"debe ser mayor a 0.");
		}
		return maxAttempts;
	}

	private int ensureValidActiveAttempt(int activeAttempt) {
		if (activeAttempt < MIN_ATTEMPT || isOverMaxAttempts()) {
			throw new InvalidEntityException("Task", "activeAttempt",
					"debe ser mayor o igual a 0 y menor o igual a maxAttempts.");
		}
		return activeAttempt;
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

	public boolean addAttempt(Attempt attempt) {
		if (attempt == null) {
			return false;
		}
		return this.attempts.add(attempt);
	}

	public void markUpdated() {
		this.timestamps.markUpdated();
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
		return attempts.stream().anyMatch(attempt -> attempt.isApproved());
	}
}
