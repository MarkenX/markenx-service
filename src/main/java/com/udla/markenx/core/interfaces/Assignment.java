package com.udla.markenx.core.interfaces;

import java.time.LocalDate;

import lombok.Getter;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.utils.validators.EntityValidator;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

@Getter
public abstract class Assignment {
	private final Long id;
	private String title;
	private String summary;
	private LocalDate dueDate;
	protected AssignmentStatus currentStatus;

	// #region Constructors

	public Assignment(String title, String summary, LocalDate dueDate) {
		this.id = null;
		setTitle(title);
		setSummary(summary);
		setDueDate(dueDate);
	}

	public Assignment(Long id, String title, String summary, LocalDate dueDate) {
		this.id = EntityValidator.ensureValidId(getClass(), id);
		setTitle(title);
		setSummary(summary);
		setDueDate(dueDate);
	}

	// #endregion Constructors

	// #region Setters

	public void setTitle(String title) {
		this.title = EntityValidator.ensureNotNullOrEmpty(getClass(), title, "title");
	}

	public void setSummary(String summary) {
		this.summary = EntityValidator.ensureNotNullOrEmpty(getClass(), summary, "summary");
	}

	public void setDueDate(LocalDate dueDate) {
		validateDueDate(dueDate);
		this.dueDate = dueDate;
		updateStatus();
	}

	// #endregion Setters

	private void validateDueDate(LocalDate dueDate) {
		if (dueDate == null || !dueDate.isAfter(LocalDate.now())) {
			throw new InvalidEntityException(getClass(), "dueDate", "debe ser una fecha futura.");
		}
	}

	public abstract void updateStatus();
}
