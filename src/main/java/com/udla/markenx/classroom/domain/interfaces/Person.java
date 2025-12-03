package com.udla.markenx.classroom.domain.interfaces;

import java.time.LocalDateTime;
import java.util.UUID;

import com.udla.markenx.shared.domain.model.DomainBaseModel;
import com.udla.markenx.shared.domain.util.EntityValidator;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

public abstract class Person extends DomainBaseModel {
	private final String firstName;
	private final String lastName;

	public Person(UUID id, String code, EntityStatus status, String firstName, String lastName, String createdBy,
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