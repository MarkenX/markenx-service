package com.udla.markenx.core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import com.udla.markenx.core.exceptions.InvalidEmailException;
import com.udla.markenx.core.interfaces.Person;
import com.udla.markenx.core.utils.validators.EntityValidator;
import com.udla.markenx.core.valueobjects.AuditInfo;

public class Student extends Person {
	private static final Class<Student> CLAZZ = Student.class;

	private final Long id;
	private final Long courseId;
	private final String email;
	private final List<StudentTask> assignedTaks;
	private final AuditInfo auditInfo;

	private Student(Long id, Long courseId, String firstName, String LastName, String email,
			List<StudentTask> assignedTasks, AuditInfo auditInfo) {
		super(firstName, LastName);
		this.id = id;
		this.courseId = courseId;
		this.email = validateEmail(email);
		this.assignedTaks = requireStudentTasks(assignedTasks);
		this.auditInfo = requireAuditInfo(auditInfo);
	}

	public Student(Long id, Long courseId, String firstName, String lastName, String email, List<StudentTask> tasks,
			String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this(id, courseId, firstName, lastName, email, tasks, new AuditInfo(createdBy, createdAt, updatedAt));
	}

	public Student(String firstName, String lastName, String email) {
		this(null, null, firstName, lastName, email, new ArrayList<>(), new AuditInfo());
	}

	public Long getId() {
		return this.id;
	}

	public String getEmail() {
		return email;
	}

	public Long getCourseId() {
		return courseId;
	}

	public String getCreatedBy() {
		return this.auditInfo.getCreatedBy();
	}

	public LocalDateTime getCreatedAt() {
		return this.auditInfo.getCreatedDateTime();
	}

	public LocalDateTime getUpdatedAt() {
		return this.auditInfo.getUpdatedDateTime();
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

	private AuditInfo requireAuditInfo(AuditInfo auditInfo) {
		return EntityValidator.ensureNotNull(CLAZZ, auditInfo, "auditInfo");
	}

	public void markUpdated() {
		this.auditInfo.markUpdated();
	}
}
