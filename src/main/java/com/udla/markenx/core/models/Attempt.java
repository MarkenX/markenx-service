package com.udla.markenx.core.models;

import java.time.Duration;
import java.time.LocalDate;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.exceptions.NullFieldException;
import com.udla.markenx.core.valueobjects.Score;
import com.udla.markenx.core.valueobjects.enums.AttemptResult;
import com.udla.markenx.core.valueobjects.enums.AttemptStatus;

import lombok.Getter;

@Getter
public class Attempt {
	private Long id;
	private final Score score;
	private final LocalDate date;
	private final Duration duration;
	private final AttemptResult result;
	private final AttemptStatus currentStatus;

	// #region Constructors

	public Attempt(LocalDate date) {
		this.score = null;
		this.date = ensureValidDate(date);
		this.duration = null;
		this.result = null;
		this.currentStatus = AttemptStatus.INTERRUPTED;
	}

	public Attempt(double score, LocalDate date, Duration duration, double minimumScoreToPass) {
		this.score = new Score(score);
		// this.date = ensureValidDate(date);
		this.date = date;
		this.duration = ensureValidDuration(duration);
		this.result = determineResult(new Score(minimumScoreToPass));
		this.currentStatus = AttemptStatus.COMPLETED;
	}

	public Attempt(double score, LocalDate date, Duration duration, AttemptResult result, AttemptStatus currenStatus) {
		this.score = new Score(score);
		// this.date = ensureValidDate(date);
		this.date = date;
		this.duration = ensureValidDuration(duration);
		this.result = result;
		this.currentStatus = currenStatus;
	}

	// #endregion Constructors

	// #region Getters

	public double getScore() {
		return this.score.value();
	}

	// #endregion

	// #region Setters

	private AttemptResult determineResult(Score minimumScoreToPass) {
		if (this.score.value() >= minimumScoreToPass.value()) {
			return AttemptResult.APPROVED;
		}
		return AttemptResult.DISAPPROVED;
	}
	// #endregion Setters

	// #region Validations

	private LocalDate ensureValidDate(LocalDate date) {
		LocalDate today = LocalDate.now();
		if (date == null) {
			throw new NullFieldException(getClass(), "date");
		}
		if (!date.isEqual(today)) {
			throw new InvalidEntityException(getClass(), "date",
					"debe ser la fecha actual (" + today + ").");
		}
		return date;
	}

	private Duration ensureValidDuration(Duration duration) {
		if (duration == null) {
			throw new NullFieldException(getClass(), "duration");
		}
		if (duration.isNegative() || duration.isZero()) {
			throw new InvalidEntityException(getClass(), "duration",
					"debe ser mayor que cero.");
		}
		return duration;
	}

	// #endregion Validations

	public boolean isApproved() {
		return this.result == AttemptResult.APPROVED;
	}
}
