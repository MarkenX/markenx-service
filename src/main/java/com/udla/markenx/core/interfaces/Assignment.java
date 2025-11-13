package com.udla.markenx.core.interfaces;

import java.time.LocalDate;

import lombok.Getter;

import com.udla.markenx.core.exceptions.InvalidEntityException;
import com.udla.markenx.core.utils.validators.EntityValidator;

@Getter
public abstract class Assignment {
	private final Long id;
	private String title;
	private String summary;
	private LocalDate dueDate;

	public Assignment(Long id, String title, String summary, LocalDate dueDate) {
		this.id = id;
		setTitle(title);
		setSummary(summary);
		setDueDate(dueDate);
	}

	public Assignment(String title, String summary, LocalDate dueDate) {
		this(null, title, summary, dueDate);
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
