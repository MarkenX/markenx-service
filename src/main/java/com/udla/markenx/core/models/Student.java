package com.udla.markenx.core.models;

import com.udla.markenx.core.interfaces.Person;
import com.udla.markenx.core.utils.validators.EntityValidator;

public class Student extends Person {
	private final String email;

	// Constructor for new students (without ID)
	public Student(String firstName, String lastName) {
		super(firstName, lastName);
		this.email = null;
	}

	// Constructor for new students with email
	public Student(String firstName, String lastName, String email) {
		super(firstName, lastName);
		this.email = validateEmail(email);
	}

	// Constructor for existing students (with ID)
	public Student(Long id, String firstName, String lastName) {
		super(id, firstName, lastName);
		this.email = null;
	}

	// Constructor for existing students with email
	public Student(Long id, String firstName, String lastName, String email) {
		super(id, firstName, lastName);
		this.email = validateEmail(email);
	}

	public String getEmail() {
		return email;
	}

	private String validateEmail(String email) {
		if (email == null) {
			return null;
		}
		return EntityValidator.ensureNotNullOrEmpty(getClass(), email, "email");
	}
}
