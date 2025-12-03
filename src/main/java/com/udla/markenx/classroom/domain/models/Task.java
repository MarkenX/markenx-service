package com.udla.markenx.classroom.domain.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.EqualsAndHashCode;

import com.udla.markenx.classroom.domain.exceptions.InvalidEntityException;
import com.udla.markenx.classroom.domain.interfaces.Assignment;
import com.udla.markenx.classroom.domain.valueobjects.Score;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

@EqualsAndHashCode(callSuper = true)
public class Task extends Assignment {
	private static final Class<Task> CLAZZ = Task.class;
	private static final String PREFIX = "TSK";
	private static final int MIN_ATTEMPT = 0;

	private final String code;
	private final UUID courseId;
	private final int academicTermYear;
	private int maxAttempts;
	private Score minScoreToPass;

	public Task(UUID id, String code, Long sequence, EntityStatus status, UUID courseId, int academicTermYear,
			String title, String summary, LocalDate dueDate, int maxAttempts, double minScoreToPass, String createdBy,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(id, code, sequence, status, title, summary, dueDate, createdBy, createdAt, updatedAt);
		this.courseId = courseId;
		this.academicTermYear = academicTermYear;
		this.maxAttempts = validateMaxAttempts(maxAttempts);
		this.minScoreToPass = new Score(minScoreToPass);
		this.code = code; // Allow null, will be generated after persistence
	}

	public Task(UUID courseId, int academicTermYear, String title, String summary, LocalDate dueDate, int maxAttempts,
			double minScoreToPass, String createdBy) {
		super(title, summary, dueDate, createdBy);
		this.courseId = courseId;
		this.academicTermYear = academicTermYear;
		this.maxAttempts = validateMaxAttempts(maxAttempts);
		this.minScoreToPass = new Score(minScoreToPass);
		this.code = null; // Code will be generated after persistence
	}

	public Task(UUID courseId, int academicTermYear, String title, String summary, LocalDate dueDate, int maxAttempts,
			double minScoreToPass) {
		super(title, summary, dueDate);
		this.courseId = courseId;
		this.academicTermYear = academicTermYear;
		this.maxAttempts = validateMaxAttempts(maxAttempts);
		this.minScoreToPass = new Score(minScoreToPass);
		this.code = null; // Code will be generated after persistence
	}

	/**
	 * Constructor para crear tareas con fechas históricas (en el pasado).
	 * Útil para seeders y datos de prueba que simulan tareas completadas.
	 * 
	 * @param allowPastDate true para permitir fechas en el pasado (omite validación
	 *                      de fecha futura)
	 */
	public Task(UUID courseId, int academicTermYear, String title, String summary, LocalDate dueDate,
			int maxAttempts, double minScoreToPass, String createdBy, boolean allowPastDate) {
		super(title, summary, dueDate, allowPastDate, createdBy);
		this.courseId = courseId;
		this.academicTermYear = academicTermYear;
		this.maxAttempts = validateMaxAttempts(maxAttempts);
		this.minScoreToPass = new Score(minScoreToPass);
		this.code = null; // Code will be generated after persistence
	}

	@Override
	public String getCode() {
		return this.code;
	}

	public UUID getCourseId() {
		return courseId;
	}

	public int getMaxAttempts() {
		return this.maxAttempts;
	}

	public double getMinScoreToPass() {
		return this.minScoreToPass.value();
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = validateMaxAttempts(maxAttempts);
	}

	public void setMinimumScoreToPass(double minimumScoreToPass) {
		this.minScoreToPass = new Score(minimumScoreToPass);
	}

	private int validateMaxAttempts(int maxAttempts) {
		if (maxAttempts <= MIN_ATTEMPT) {
			throw new InvalidEntityException(CLAZZ, "maxAttempts", "debe ser mayor a 0.");
		}
		return maxAttempts;
	}

	@Override
	protected String generateCode() {
		if (serialNumber == null) {
			return null; // Code will be generated after persistence
		}
		return String.format("%s-%d-%04d", PREFIX, academicTermYear, serialNumber);
	}

	public static String generateCodeFromId(Long id, int academicTermYear) {
		if (id == null) {
			return null;
		}
		return String.format("%s-%d-%04d", PREFIX, academicTermYear, id);
	}
}
