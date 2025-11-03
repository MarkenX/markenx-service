package com.udla.markenx.application.interfaces.out.repository;

import org.springframework.data.domain.Page;

import com.udla.markenx.core.models.Attempt;

public interface AttemptRepositoryPort {
    Page<Attempt> getAttemptsByTaskId(Long taskId, int page, int size);
}
