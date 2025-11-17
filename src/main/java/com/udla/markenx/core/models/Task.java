package com.udla.markenx.core.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.interfaces.Assignment;
import com.udla.markenx.core.valueobjects.Score;
import com.udla.markenx.core.valueobjects.enums.DomainBaseModelStatus;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Task extends Assignment {
	private static final Class<Task> CLAZZ = Task.class;
	private static final String PREFIX = "TSK";
	private static final int MIN_ATTEMPT = 0;

	private final String code;
	private final UUID courseId;
	private final int academicTermYear;
	private int maxAttempts;
	private Score minScoreToPass;

	public Task(UUID id, String code, Long sequence, DomainBaseModelStatus status, UUID courseId, int academicTermYear,
			String title, String summary, LocalDate dueDate, int maxAttempts, double minScoreToPass, String createdBy,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(courseId, code, sequence, status, title, summary, dueDate, createdBy, createdAt, updatedAt);
		this.courseId = courseId;
		this.academicTermYear = academicTermYear;
		this.maxAttempts = validateMaxAttempts(maxAttempts);
		this.minScoreToPass = new Score(minScoreToPass);
		this.code = requireCode(code);
	}

	public Task(UUID courseId, int academicTermYear, String title, String summary, LocalDate dueDate, int maxAttempts,
			double minScoreToPass, String createdBy) {
		super(title, summary, dueDate, createdBy);
		this.courseId = courseId;
		this.academicTermYear = academicTermYear;
		this.maxAttempts = validateMaxAttempts(maxAttempts);
		this.minScoreToPass = new Score(minScoreToPass);
		this.code = generateCode();
	}

	public Task(UUID courseId, int academicTermYear, String title, String summary, LocalDate dueDate, int maxAttempts,
			double minScoreToPass) {
		super(title, summary, dueDate);
		this.courseId = courseId;
		this.academicTermYear = academicTermYear;
		this.maxAttempts = validateMaxAttempts(maxAttempts);
		this.minScoreToPass = new Score(minScoreToPass);
		this.code = generateCode();
	}

	@Override
	public String getCode() {
		return this.code;
	}

	public UUID getCourseId() {
		return courseId;
	}

	public int getMaxAttempts() {
		return this.maxAttempts;
	}

	public double getMinScoreToPass() {
		return this.minScoreToPass.value();
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = validateMaxAttempts(maxAttempts);
	}

	public void setMinimumScoreToPass(double minimumScoreToPass) {
		this.minScoreToPass = new Score(minimumScoreToPass);
	}

	private int validateMaxAttempts(int maxAttempts) {
		if (maxAttempts <= MIN_ATTEMPT) {
			throw new InvalidEntityException(CLAZZ, "maxAttempts", "debe ser mayor a 0.");
		}
		return maxAttempts;
	}

	@Override
	protected String generateCode() {
		return String.format("%s-%d-%04d", PREFIX, academicTermYear, serialNumber);
	}
}
