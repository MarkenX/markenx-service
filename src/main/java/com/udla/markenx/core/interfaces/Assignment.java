package com.udla.markenx.core.interfaces;

import java.time.LocalDate;

import lombok.Getter;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

@Getter
public abstract class Assignment {
	private Long id;
	private String title;
	private String summary;
	private LocalDate dueDate;
	protected AssignmentStatus currentStatus;

	// #region Constructors

	public Assignment(String title, String summary, LocalDate dueDate) {
		this.title = title;
		this.summary = summary;
		setDueDate(dueDate);
	}

	public Assignment(long id, String title, String summary, LocalDate dueDate) {
		this.id = id;
		this.title = title;
		this.summary = summary;
		setDueDate(dueDate);
	}

	// #endregion Constructors

	// #region Setters

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setDueDate(LocalDate dueDate) {
		validateDueDate(dueDate);
		this.dueDate = dueDate;
		updateStatus();
	}

	// #endregion Setters

	private void validateDueDate(LocalDate dueDate) {
		if (dueDate == null || !dueDate.isAfter(LocalDate.now())) {
			throw new InvalidEntityException("Assignment", "dueDate",
					"debe ser una fecha futura.");
		}
	}

	public abstract void updateStatus();
}
