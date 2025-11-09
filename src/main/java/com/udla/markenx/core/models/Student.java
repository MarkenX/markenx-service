package com.udla.markenx.core.models;

import com.udla.markenx.core.interfaces.Person;
import com.udla.markenx.core.utils.validators.EntityValidator;

public class Student extends Person {
	private final String email;
	private final Long courseId;

	// Constructor for new students
	public Student(String firstName, String lastName) {
		super(firstName, lastName);
		this.email = null;
		this.courseId = null;
	}

	// Constructor for new students with email
	public Student(String firstName, String lastName, String email) {
		super(firstName, lastName);
		this.email = validateEmail(email);
		this.courseId = null;
	}

	// Constructor for new students with email and courseId
	public Student(String firstName, String lastName, String email, Long courseId) {
		super(firstName, lastName);
		this.email = validateEmail(email);
		this.courseId = courseId;
	}

	// Constructor for existing students (with ID)
	public Student(Long id, String firstName, String lastName) {
		super(id, firstName, lastName);
		this.email = null;
		this.courseId = null;
	}

	// Constructor for existing students with email
	public Student(Long id, String firstName, String lastName, String email) {
		super(id, firstName, lastName);
		this.email = validateEmail(email);
		this.courseId = null;
	}

	// Constructor for existing students with email and courseId
	public Student(Long id, String firstName, String lastName, String email, Long courseId) {
		super(id, firstName, lastName);
		this.email = validateEmail(email);
		this.courseId = courseId;
	}

	public String getEmail() {
		return email;
	}

	public Long getCourseId() {
		return courseId;
	}

	private String validateEmail(String email) {
		if (email == null) {
			return null;
		}
		return EntityValidator.ensureNotNullOrEmpty(getClass(), email, "email");
	}
}
