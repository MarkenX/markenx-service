package com.udla.markenx.core.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.interfaces.Assignment;
import com.udla.markenx.core.utils.validators.EntityValidator;
import com.udla.markenx.core.valueobjects.Score;
import com.udla.markenx.core.valueobjects.AuditInfo;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Task extends Assignment {
	private static final Class<Task> CLAZZ = Task.class;
	private static final int MIN_ATTEMPT = 0;

	private final Long id;
	private final Long courseId;
	private int maxAttempts;
	private Score minimumScoreToPass;
	private final AuditInfo auditInfo;

	private Task(Long id, Long courseId, String title, String summary, LocalDate dueDate, int maxAttempts, double score,
			AuditInfo auditInfo) {
		super(title, summary, dueDate);
		this.id = id;
		this.courseId = courseId;
		this.maxAttempts = validateMaxAttempts(maxAttempts);
		this.minimumScoreToPass = new Score(score);
		this.auditInfo = requireAuditInfo(auditInfo);
	}

	public Task(Long id, Long courseId, String title, String summary, LocalDate dueDate, int maxAttempts, double score,
			String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this(id, courseId, title, summary, dueDate, maxAttempts, score, new AuditInfo(createdBy, createdAt, updatedAt));
	}

	public Task(String title, String summary, LocalDate dueDate, int maxAttempts, double score) {
		this(null, null, title, summary, dueDate, maxAttempts, score, new AuditInfo());
	}

	public Long getId() {
		return this.id;
	}

	public Long getCourseId() {
		return courseId;
	}

	public int getMaxAttempts() {
		return this.maxAttempts;
	}

	public double getMinimumScoreToPass() {
		return this.minimumScoreToPass.value();
	}

	public String getCreatedBy() {
		return this.auditInfo.getCreatedBy();
	}

	public LocalDateTime getCreatedAt() {
		return this.auditInfo.getCreatedDateTime();
	}

	public LocalDateTime getUpdatedAt() {
		return this.auditInfo.getUpdatedDateTime();
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = validateMaxAttempts(maxAttempts);
	}

	public void setMinimumScoreToPass(double minimumScoreToPass) {
		this.minimumScoreToPass = new Score(minimumScoreToPass);
	}

	private int validateMaxAttempts(int maxAttempts) {
		if (maxAttempts <= MIN_ATTEMPT) {
			throw new InvalidEntityException(CLAZZ, "maxAttempts", "debe ser mayor a 0.");
		}
		return maxAttempts;
	}

	private AuditInfo requireAuditInfo(AuditInfo auditInfo) {
		return EntityValidator.ensureNotNull(CLAZZ, auditInfo, "auditInfo");
	}

	public void markUpdated() {
		this.auditInfo.markUpdated();
	}
}
