package com.udla.markenx.application.services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.udla.markenx.application.ports.out.persistance.repositories.AttemptRepositoryPort;
import com.udla.markenx.core.models.Attempt;

@Service
public class TaskService {
	private final AttemptRepositoryPort attemptRepository;

	public TaskService(AttemptRepositoryPort attemptRepository) {
		this.attemptRepository = attemptRepository;
	}

	public Page<Attempt> getTaskAttempts(Long taskId, int page, int size) {
		if (taskId == null) {
			throw new IllegalArgumentException("El código del estudiante no puede ser nulo.");
		}
		return attemptRepository.getAttemptsByTaskId(taskId, page, size);
	}
}
