package com.udla.markenx.core.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.utils.validators.EntityValidator;
import com.udla.markenx.core.valueobjects.enums.DomainBaseModelStatus;

@Getter
public abstract class Assignment extends DomainBaseModel {

	private String title;
	private String summary;
	private LocalDate dueDate;

	public Assignment(UUID id, String code, Long sequence, DomainBaseModelStatus status, String title, String summary,
			LocalDate dueDate, String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(id, code, status, createdBy, createdAt, updatedAt);
		setTitle(title);
		setSummary(summary);
		setDueDate(dueDate);
	}

	public Assignment(String title, String summary, LocalDate dueDate, String createdBy) {
		super(createdBy);
		setTitle(title);
		setSummary(summary);
		setDueDate(dueDate);
	}

	public abstract String getCode();

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
