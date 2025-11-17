package com.udla.markenx.classroom.core.interfaces;

import java.time.LocalDateTime;
import java.util.UUID;

import com.udla.markenx.classroom.core.utils.validators.EntityValidator;
import com.udla.markenx.classroom.core.valueobjects.enums.DomainBaseModelStatus;

public abstract class Person extends DomainBaseModel {
	private final String firstName;
	private final String lastName;

	public Person(UUID id, String code, DomainBaseModelStatus status, String firstName, String lastName, String createdBy,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(id, code, status, createdBy, createdAt, updatedAt);
		this.firstName = EntityValidator.ensureNotNullOrEmpty(getClass(), firstName, "firstName");
		this.lastName = EntityValidator.ensureNotNullOrEmpty(getClass(), lastName, "lastName");
	}

	public Person(String firstName, String lastName, String createdBy) {
		super(createdBy);
		this.firstName = EntityValidator.ensureNotNullOrEmpty(getClass(), firstName, "firstName");
		this.lastName = EntityValidator.ensureNotNullOrEmpty(getClass(), lastName, "lastName");
	}

	public Person(String firstName, String lastName) {
		super();
		this.firstName = EntityValidator.ensureNotNullOrEmpty(getClass(), firstName, "firstName");
		this.lastName = EntityValidator.ensureNotNullOrEmpty(getClass(), lastName, "lastName");
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getFullName() {
		return firstName + ' ' + lastName;
	}
}