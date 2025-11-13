package com.udla.markenx.core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import com.udla.markenx.core.exceptions.InvalidEmailException;
import com.udla.markenx.core.interfaces.Person;
import com.udla.markenx.core.utils.validators.EntityValidator;
import com.udla.markenx.core.valueobjects.enums.DomainBaseModelStatus;

public class Student extends Person {
	private static final Class<Student> CLAZZ = Student.class;
	private static final String PREFIX = "STD";

	private final String code;
	private final Long sequence;
	private final UUID courseId;
	private final String email;
	private final List<StudentTask> assignedTaks;

	private Student(UUID id, String code, Long sequence, DomainBaseModelStatus status, UUID courseId, String firstName,
			String lastName, String email, List<StudentTask> assignedTasks, String createdBy, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super(id, code, status, firstName, lastName, createdBy, createdAt, updatedAt);
		this.sequence = sequence;
		this.courseId = courseId;
		this.email = validateEmail(email);
		this.assignedTaks = requireStudentTasks(assignedTasks);
		this.code = requireCode(code);
	}

	public Student(UUID courseId, String firstName, String lastName, String email, String createdBy) {
		super(firstName, lastName, createdBy);
		this.sequence = null;
		this.courseId = courseId;
		this.email = validateEmail(email);
		this.assignedTaks = new ArrayList<>();
		this.code = generateCode();
	}

	public Student(UUID courseId, String firstName, String lastName, String email) {
		super(firstName, lastName);
		this.sequence = null;
		this.courseId = courseId;
		this.email = validateEmail(email);
		this.assignedTaks = new ArrayList<>();
		this.code = generateCode();
	}

	public String getCode() {
		return this.code;
	}

	public String getEmail() {
		return email;
	}

	public UUID getCourseId() {
		return courseId;
	}

	public List<StudentTask> getAssignedTaks() {
		return List.copyOf(this.assignedTaks);
	}

	private String validateEmail(String email) {
		String validatedEmail = EntityValidator.ensureNotNullOrEmpty(CLAZZ, email, "email");
		EmailValidator validator = new EmailValidator();
		if (!validator.isValid(validatedEmail, null)) {
			throw new InvalidEmailException("El formato del correo electrónico no es válido:" + validatedEmail);
		}
		return validatedEmail;
	}

	private List<StudentTask> requireStudentTasks(List<StudentTask> tasks) {
		return EntityValidator.ensureNotNull(CLAZZ, assignedTaks, "assignedTaks");
	}

	@Override
	protected String generateCode() {
		return String.format("%s-%06d", PREFIX, sequence);
	}
}
