package com.udla.markenx.core.interfaces;

import lombok.Getter;

import com.udla.markenx.core.utils.validators.EntityValidator;

@Getter
public class Person {
	private long id;
	private final String firstName;
	private final String lastName;

	public Person(String firstName, String lastName) {
		this.firstName = EntityValidator.ensureNotNullOrEmpty(getClass(), firstName, "firstName");
		this.lastName = EntityValidator.ensureNotNullOrEmpty(getClass(), lastName, "lastName");
	}

	public Person(Long id, String firstName, String lastName) {
		this.id = EntityValidator.ensureValidId(getClass(), id);
		this.firstName = EntityValidator.ensureNotNullOrEmpty(getClass(), firstName, "firstName");
		this.lastName = EntityValidator.ensureNotNullOrEmpty(getClass(), lastName, "lastName");
	}

	public String getFullName() {
		return firstName + ' ' + lastName;
	}
}