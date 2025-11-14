package com.udla.markenx.core.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.interfaces.DomainBaseModel;
import com.udla.markenx.core.utils.validators.EntityValidator;
import com.udla.markenx.core.valueobjects.Score;

import com.udla.markenx.core.valueobjects.enums.AttemptResult;
import com.udla.markenx.core.valueobjects.enums.AttemptStatus;
import com.udla.markenx.core.valueobjects.enums.DomainBaseModelStatus;

public class Attempt extends DomainBaseModel {
	private static final Class<Attempt> CLAZZ = Attempt.class;
	private static final String PREFIX = "ATM";

	private final String code;
	private final Long sequence;
	private final Long studentSequence;
	private final Long taskSequence;
	private final UUID studentTaskId;
	private final Score taskMinScoreToPass;
	private final Score score;
	private final Duration timeSpent;
	private final AttemptResult result;
	private final AttemptStatus attemptStatus;

	public Attempt(UUID id, String code, Long sequence, DomainBaseModelStatus status, UUID studentTaskId,
			Long studentSequence,
			Long taskSequence, double taskMinScoreToPass, double score, Duration timeSpent, AttemptResult result,
			AttemptStatus attemptStatus, String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(id, code, status, createdBy, createdAt, updatedAt);
		this.sequence = sequence;
		this.studentTaskId = studentTaskId;
		this.studentSequence = studentSequence;
		this.taskMinScoreToPass = new Score(taskMinScoreToPass);
		this.taskSequence = taskSequence;
		this.score = new Score(score);
		this.timeSpent = validateDuration(timeSpent);
		this.attemptStatus = requireAttemptStatus(attemptStatus);
		this.result = determineResult();
		this.code = requireCode(code);
	}

	public Attempt(Long studentSequence, Long taskSequence, double taskMinScoreToPass, double score,
			Duration timeSpent, AttemptStatus attemptStatus, UUID studentTaskId, String createdBy) {
		super(createdBy);
		this.sequence = null;
		this.studentTaskId = studentTaskId;
		this.studentSequence = studentSequence;
		this.taskSequence = taskSequence;
		this.taskMinScoreToPass = new Score(taskMinScoreToPass);
		this.score = new Score(score);
		this.timeSpent = validateDuration(timeSpent);
		this.attemptStatus = requireAttemptStatus(attemptStatus);
		this.result = determineResult();
		this.code = generateCode();
	}

	public Attempt(UUID studentTaskId, Long studentSequence, Long taskSequence, double taskMinScoreToPass, double score,
			Duration timeSpent, AttemptStatus attemptStatus) {
		super();
		this.sequence = null;
		this.studentTaskId = studentTaskId;
		this.studentSequence = studentSequence;
		this.taskSequence = taskSequence;
		this.taskMinScoreToPass = new Score(taskMinScoreToPass);
		this.score = new Score(score);
		this.timeSpent = validateDuration(timeSpent);
		this.attemptStatus = requireAttemptStatus(attemptStatus);
		this.result = determineResult();
		this.code = generateCode();
	}

	public UUID getStudentTaskId() {
		return this.studentTaskId;
	}

	public String getCode() {
		return this.code;
	}

	public Long getSequence() {
		return this.sequence;
	}

	public double getScore() {
		return this.score.value();
	}

	public Duration getTimeSpent() {
		return this.timeSpent;
	}

	public AttemptResult getResult() {
		return this.result;
	}

	public AttemptStatus getAttemptStatus() {
		return this.attemptStatus;
	}

	private AttemptStatus requireAttemptStatus(AttemptStatus attemptStatus) {
		return EntityValidator.ensureNotNull(CLAZZ, attemptStatus, "attemptStatus");
	}

	private AttemptResult determineResult() {
		if (this.attemptStatus == AttemptStatus.INTERRUPTED) {
			return null;
		}
		if (this.score.value() >= this.taskMinScoreToPass.value()) {
			return AttemptResult.APPROVED;
		}
		return AttemptResult.DISAPPROVED;
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

	@Override
	protected String generateCode() {
		return String.format("%s-%04d-STD%06d-%02d", PREFIX, taskSequence, studentSequence, sequence);
	}
}
