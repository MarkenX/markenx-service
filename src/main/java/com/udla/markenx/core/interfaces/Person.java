package com.udla.markenx.core.interfaces;

import lombok.Getter;

import com.udla.markenx.core.exceptions.InvalidEntityException;

@Getter
public class Person {
	private final long id;
	private final String firstName;
	private final String lastName;

	public Person(long id, String firstName, String lastName) {
		validateId(id);
		this.firstName = validateAndNormalizeString(firstName, "firstName");
		this.lastName = validateAndNormalizeString(lastName, "lastName");
		this.id = id;
	}

	// #region Validations

	private void validateId(long id) {
		if (id <= 0) {
			throw new InvalidEntityException("Person", "id",
					"debe ser mayor a 0");
		}
	}

	private String validateAndNormalizeString(String value, String fieldName) {
		if (value == null) {
			throw new InvalidEntityException("Person", fieldName,
					"no puede ser nulo");
		}

		String normalized = value.trim();

		if (normalized.isEmpty()) {
			throw new InvalidEntityException("Person", fieldName,
					"no puede estar vacío");
		}

		return normalized;
	}

	// #endregion Validations

	public String getFullName() {
		return firstName + ' ' + lastName;
	}
}