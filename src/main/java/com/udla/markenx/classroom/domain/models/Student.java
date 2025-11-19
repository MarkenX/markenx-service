package com.udla.markenx.classroom.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import com.udla.markenx.classroom.domain.exceptions.InvalidEmailException;
import com.udla.markenx.classroom.domain.interfaces.Person;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

public class Student extends Person {
	// #region Constants

	private static final Class<Student> CLAZZ = Student.class;
	private static final String PREFIX = "STD";

	// #endregion

	private final String code;
	private final Long serialNumber;
	private final UUID enrolledCourseId;
	private final String academicEmail;
	private final List<StudentTask> assignedTasks;

	// #region Constructors

	public Student(
			UUID id,
			String code,
			Long serialNumber,
			DomainBaseModelStatus status,
			UUID enrolledCourseId,
			String firstName,
			String lastName,
			String email,
			List<StudentTask> assignedTasks,
			String createdBy,
			LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super(id, code, status, firstName, lastName, createdBy, createdAt, updatedAt);
		this.serialNumber = serialNumber;
		this.enrolledCourseId = enrolledCourseId;
		this.academicEmail = validateEmail(email);
		this.assignedTasks = requireStudentTasks(assignedTasks);
		this.code = requireCode(code);
	}

	// Lightweight constructor for simple DTOs (without loading tasks)
	public Student(
			UUID id,
			String code,
			Long serialNumber,
			DomainBaseModelStatus status,
			UUID enrolledCourseId,
			String firstName,
			String lastName,
			String email,
			String createdBy,
			LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super(id, code, status, firstName, lastName, createdBy, createdAt, updatedAt);
		this.serialNumber = serialNumber;
		this.enrolledCourseId = enrolledCourseId;
		this.academicEmail = validateEmail(email);
		this.assignedTasks = new ArrayList<>();
		this.code = requireCode(code);
	}

	public Student(
			UUID courseId,
			String firstName,
			String lastName,
			String email,
			List<StudentTask> assignedTasks,
			String createdBy) {
		super(firstName, lastName, createdBy);
		this.serialNumber = null;
		this.enrolledCourseId = courseId;
		this.academicEmail = validateEmail(email);
		this.assignedTasks = new ArrayList<>();
		this.code = null; // Code will be generated after persistence
	}

	public Student(
			UUID courseId,
			String firstName,
			String lastName,
			String email) {
		super(firstName, lastName);
		this.serialNumber = null;
		this.enrolledCourseId = courseId;
		this.academicEmail = validateEmail(email);
		this.assignedTasks = new ArrayList<>();
		this.code = null; // Code will be generated after persistence
	}

	// #endregion

	// #region Getters

	public String getCode() {
		return this.code;
	}

	public String getAcademicEmail() {
		return academicEmail;
	}

	public UUID getEnrolledCourseId() {
		return enrolledCourseId;
	}

	public Long getSerialNumber() {
		return this.serialNumber;
	}

	public List<StudentTask> getAssignedTasks() {
		return List.copyOf(this.assignedTasks);
	}

	// #endregion

	// #region Setters

	public void addAssignTasks(List<StudentTask> assignedTasks) {
		List<StudentTask> validated = EntityValidator.ensureNotNull(CLAZZ, assignedTasks, "assigned tasks");
		this.assignedTasks.addAll(List.copyOf(validated));
	}

	// #endregion

	// #region Validations

	private String validateEmail(String email) {
		String validatedEmail = EntityValidator.ensureNotNullOrEmpty(CLAZZ, email, "email");
		EmailValidator validator = new EmailValidator();
		if (!validator.isValid(validatedEmail, null)) {
			throw new InvalidEmailException("El formato del correo electrónico no es válido:" + validatedEmail);
		}
		return validatedEmail;
	}

	private List<StudentTask> requireStudentTasks(List<StudentTask> tasks) {
		return EntityValidator.ensureNotNull(CLAZZ, tasks, "assignedTasks");
	}

	// #endregion

	@Override
	protected String generateCode() {
		if (serialNumber == null) {
			return null; // Code will be generated after persistence
		}
		return String.format("%s-%06d", PREFIX, serialNumber);
	}

	public static String generateCodeFromId(Long id) {
		if (id == null) {
			return null;
		}
		return String.format("%s-%06d", PREFIX, id);
	}
}
