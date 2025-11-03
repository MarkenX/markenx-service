package com.udla.markenx.application.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.udla.markenx.application.interfaces.out.repositories.AttemptRepositoryPort;
import com.udla.markenx.application.interfaces.out.repositories.TaskRepositoryPort;
import com.udla.markenx.core.enums.AssignmentStatus;
import com.udla.markenx.core.models.Attempt;
import com.udla.markenx.core.models.Task;

@Service
public class TaskService {
	private final TaskRepositoryPort taskRepository;
	private final AttemptRepositoryPort attemptRepository;

	public TaskService(TaskRepositoryPort taskRepository, AttemptRepositoryPort attemptRepository) {
		this.taskRepository = taskRepository;
		this.attemptRepository = attemptRepository;
	}

	public Page<Task> getStudentTasks(Long studentId, LocalDate startDate, LocalDate endDate, AssignmentStatus status,
			int page,
			int size) {
		if (studentId == null) {
			throw new IllegalArgumentException("El código del estudiante no puede ser nulo.");
		}

		if (page < 0)
			page = 0;
		if (size <= 0 || size > 100)
			size = 10;

		if (startDate != null && endDate != null && status != null) {
			return taskRepository.getStudentTasksByDueDateAndStatus(studentId, startDate, endDate, status, page, size);
		} else if (startDate != null && endDate != null) {
			return taskRepository.getStudentTasksByDueDate(studentId, startDate, endDate, page, size);
		} else if (status != null) {
			return taskRepository.getStudentTasksByStatus(studentId, status, page, size);
		} else {
			return taskRepository.getTasksByStudentId(studentId, page, size);
		}
	}

	public Page<Attempt> getTaskAttempts(Long taskId, int page, int size) {
		if (taskId == null) {
			throw new IllegalArgumentException("El código del estudiante no puede ser nulo.");
		}
		return attemptRepository.getAttemptsByTaskId(taskId, page, size);
	}
}
