package com.udla.markenx.core.interfaces;

import lombok.Getter;

import com.udla.markenx.core.exceptions.NullFieldException;
import com.udla.markenx.core.utils.validators.EntityValidator;

@Getter
public class Person {
	private long id;
	private final String firstName;
	private final String lastName;

	public Person(String firstName, String lastName) {
		this.firstName = validateAndNormalizeString(firstName, "firstName");
		this.lastName = validateAndNormalizeString(lastName, "lastName");
	}

	public Person(Long id, String firstName, String lastName) {
		this.id = EntityValidator.ensureValidId(getClass(), id);
		this.firstName = validateAndNormalizeString(firstName, "firstName");
		this.lastName = validateAndNormalizeString(lastName, "lastName");
	}

	// #region Validations

	private String validateAndNormalizeString(String value, String fieldName) {
		if (value == null) {
			throw new NullFieldException(getClass(), fieldName);
		}

		String normalized = value.trim();

		if (normalized.isEmpty()) {
			throw new NullFieldException(getClass(), fieldName);
		}

		return normalized;
	}

	// #endregion Validations

	public String getFullName() {
		return firstName + ' ' + lastName;
	}
}