package com.udla.markenx.classroom.application.services;

import com.udla.markenx.classroom.core.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AttemptRepositoryPort;
import com.udla.markenx.classroom.core.interfaces.StudentAssignment;
import com.udla.markenx.classroom.core.models.Attempt;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentAssignmentRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort;

@Service
public class TaskService {
	private final AttemptRepositoryPort attemptRepository;
	private final TaskRepositoryPort taskRepository;
	private final StudentAssignmentRepositoryPort studentAssignmentRepository;

	public TaskService(AttemptRepositoryPort attemptRepository,
			TaskRepositoryPort taskRepository,
			StudentAssignmentRepositoryPort studentAssignmentRepository) {
		this.attemptRepository = attemptRepository;
		this.taskRepository = taskRepository;
		this.studentAssignmentRepository = studentAssignmentRepository;
	}

	public Page<Attempt> getTaskAttempts(Long taskId, int page, int size) {
		if (taskId == null) {
			throw new IllegalArgumentException("El id de la tarea no puede ser nulo.");
		}
		// Return all attempts for a task across students
		return attemptRepository.getAttemptsByTaskId(taskId, page, size);
	}

	public Page<Attempt> getTaskAttemptsByStudent(Long taskId, Long studentId, int page, int size) {
		if (taskId == null) {
			throw new IllegalArgumentException("El id de la tarea no puede ser nulo.");
		}
		if (studentId == null) {
			throw new IllegalArgumentException("El id del estudiante no puede ser nulo.");
		}
		StudentAssignment sa = studentAssignmentRepository.getByAssignmentIdAndStudentId(taskId, studentId);
		if (sa == null) {
			return org.springframework.data.domain.Page.empty();
		}
		// return attemptRepository.getAttemptsByStudentAssignmentId(sa.getId(), page,
		// size);
		return attemptRepository.getAttemptsByStudentAssignmentId(null, page, size);
	}

	/**
	 * Create a new attempt for the given task and student. The Task's minimum score
	 * is used to determine the attempt result.
	 */
	public Attempt createAttempt(Long taskId, Long studentId, double score, java.time.LocalDate date,
			java.time.Duration duration) {
		if (taskId == null) {
			throw new IllegalArgumentException("El id de la tarea no puede ser nulo.");
		}
		if (studentId == null) {
			throw new IllegalArgumentException("El id del estudiante no puede ser nulo.");
		}

		Task task = taskRepository.getTaskById(taskId);
		double minScore = task.getMinScoreToPass();

		// // get or create student-assignment
		// StudentAssignment sa =
		// studentAssignmentRepository.getByAssignmentIdAndStudentId(taskId, studentId);
		// if (sa == null) {
		// sa = new StudentAssignment(taskId, studentId);
		// sa = studentAssignmentRepository.create(sa);
		// }

		// Attempt attempt = new Attempt(score, date, duration, minScore, sa.getId());
		return attemptRepository.createAttempt(null, null);
	}
}
