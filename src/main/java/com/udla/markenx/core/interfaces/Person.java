package com.udla.markenx.core.interfaces;

import lombok.Getter;

import com.udla.markenx.core.utils.validators.EntityValidator;

@Getter
public class Person {
	private final Long id;
	private final String firstName;
	private final String lastName;

	public Person(Long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = EntityValidator.ensureNotNullOrEmpty(getClass(), firstName, "firstName");
		this.lastName = EntityValidator.ensureNotNullOrEmpty(getClass(), lastName, "lastName");
	}

	public Person(String firstName, String lastName) {
		this(null, firstName, lastName);
	}

	public String getFullName() {
		return firstName + ' ' + lastName;
	}
}