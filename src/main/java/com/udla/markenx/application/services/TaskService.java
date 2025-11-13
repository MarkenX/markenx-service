package com.udla.markenx.application.services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.udla.markenx.application.ports.out.persistance.repositories.AttemptRepositoryPort;
import com.udla.markenx.core.interfaces.StudentAssignment;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.application.ports.out.persistance.repositories.StudentAssignmentRepositoryPort;

@Service
public class TaskService {
	private final AttemptRepositoryPort attemptRepository;
	private final com.udla.markenx.application.ports.out.persistance.repositories.TaskRepositoryPort taskRepository;
	private final StudentAssignmentRepositoryPort studentAssignmentRepository;

	public TaskService(AttemptRepositoryPort attemptRepository,
			com.udla.markenx.application.ports.out.persistance.repositories.TaskRepositoryPort taskRepository,
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
		return attemptRepository.getAttemptsByStudentAssignmentId(sa.getId(), page, size);
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

		com.udla.markenx.core.models.Task task = taskRepository.getTaskById(taskId);
		double minScore = task.getMinimumScoreToPass();

		// get or create student-assignment
		StudentAssignment sa = studentAssignmentRepository.getByAssignmentIdAndStudentId(taskId, studentId);
		if (sa == null) {
			sa = new StudentAssignment(taskId, studentId);
			sa = studentAssignmentRepository.create(sa);
		}

		Attempt attempt = new Attempt(score, date, duration, minScore, sa.getId());
		return attemptRepository.createAttempt(attempt, sa.getId());
	}
}
