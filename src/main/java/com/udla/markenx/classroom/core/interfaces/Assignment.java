package com.udla.markenx.classroom.core.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.udla.markenx.classroom.core.exceptions.InvalidEntityException;
import com.udla.markenx.shared.domain.model.DomainBaseModel;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;
import com.udla.markenx.classroom.core.valueobjects.enums.DomainBaseModelStatus;

import lombok.Getter;

@Getter
public abstract class Assignment extends DomainBaseModel {

	protected final Long serialNumber;
	private String title;
	private String summary;
	private LocalDate dueDate;

	public Assignment(UUID id, String code, Long sequence, DomainBaseModelStatus status, String title, String summary,
			LocalDate dueDate, String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(id, code, status, createdBy, createdAt, updatedAt);
		this.serialNumber = sequence;
		setTitle(title);
		setSummary(summary);
		setDueDate(dueDate);
	}

	public Assignment(String title, String summary, LocalDate dueDate, String createdBy) {
		super(createdBy);
		this.serialNumber = null;
		setTitle(title);
		setSummary(summary);
		setDueDate(dueDate);
	}

	public Assignment(String title, String summary, LocalDate dueDate) {
		super();
		this.serialNumber = null;
		setTitle(title);
		setSummary(summary);
		setDueDate(dueDate);
	}

	public abstract String getCode();

	public Long getSerialNumber() {
		return this.serialNumber;
	}

	public void setTitle(String title) {
		this.title = EntityValidator.ensureNotNullOrEmpty(getClass(), title, "title");
	}

	public void setSummary(String summary) {
		this.summary = EntityValidator.ensureNotNullOrEmpty(getClass(), summary, "summary");
	}

	public void setDueDate(LocalDate dueDate) {
		validateDueDate(dueDate);
		this.dueDate = dueDate;
	}

	private void validateDueDate(LocalDate dueDate) {
		if (dueDate == null || !dueDate.isAfter(LocalDate.now())) {
			throw new InvalidEntityException(getClass(), "dueDate", "debe ser una fecha futura.");
		}
	}

	public boolean isOverdue() {
		return getDueDate().isBefore(LocalDate.now());
	}
}
