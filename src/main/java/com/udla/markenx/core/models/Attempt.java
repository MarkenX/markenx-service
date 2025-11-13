package com.udla.markenx.core.models;

import java.time.Duration;
import java.time.LocalDate;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.utils.validators.EntityValidator;
import com.udla.markenx.core.valueobjects.Score;

import com.udla.markenx.core.valueobjects.enums.AttemptResult;
import com.udla.markenx.core.valueobjects.enums.AttemptStatus;

public class Attempt {
	private static final Class<Attempt> CLAZZ = Attempt.class;

	private final Long id;
	private final Long studentTaskId;
	private final Score score;
	private final LocalDate submittedAt;
	private final Duration timeSpent;
	private final AttemptResult result;
	private final AttemptStatus status;

	public Attempt(Long id, Long studentTaskId, double score, LocalDate submittedAt, Duration timeSpent,
			AttemptResult result, AttemptStatus status) {
		this.id = id;
		this.studentTaskId = studentTaskId;
		this.score = new Score(score);
		this.submittedAt = validateDate(submittedAt);
		this.timeSpent = validateDuration(timeSpent);
		this.result = result == null ? determineResult(new Score(0.0)) : result;
		this.status = status == null ? AttemptStatus.COMPLETED : status;
	}

	public Attempt(double score, LocalDate submittedAt, Duration timeSpent) {
		this(null, null, score, submittedAt, timeSpent, null, null);
	}

	public Long getId() {
		return this.id;
	}

	public Long getStudentTaskId() {
		return this.studentTaskId;
	}

	public double getScore() {
		return this.score.value();
	}

	public LocalDate getSubmittedAt() {
		return this.submittedAt;
	}

	public Duration getTimeSpent() {
		return this.timeSpent;
	}

	public AttemptResult getResult() {
		return this.result;
	}

	public AttemptStatus getStatus() {
		return this.status;
	}

	private AttemptResult determineResult(Score minimumScoreToPass) {
		if (this.score.value() >= minimumScoreToPass.value()) {
			return AttemptResult.APPROVED;
		}
		return AttemptResult.DISAPPROVED;
	}

	private LocalDate validateDate(LocalDate submittedAt) {
		LocalDate today = LocalDate.now();
		EntityValidator.ensureNotNull(CLAZZ, submittedAt, "submittedAt");
		if (!submittedAt.isEqual(today)) {
			throw new InvalidEntityException(CLAZZ, "date", "debe ser la fecha actual (" + today + ").");
		}
		return submittedAt;
	}

	private Duration validateDuration(Duration timeSpent) {
		EntityValidator.ensureNotNull(CLAZZ, timeSpent, "timeSpent");
		if (timeSpent.isNegative() || timeSpent.isZero()) {
			throw new InvalidEntityException(CLAZZ, "duration", "debe ser mayor que cero.");
		}
		return timeSpent;
	}

	public boolean isApproved() {
		return this.result == AttemptResult.APPROVED;
	}
}
