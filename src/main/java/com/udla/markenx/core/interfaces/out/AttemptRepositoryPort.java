package com.udla.markenx.core.interfaces.out;

import org.springframework.data.domain.Page;

import com.udla.markenx.core.model.Attempt;

public interface AttemptRepositoryPort {
    Page<Attempt> getAttemptsByTaskId(Long taskId, int page, int size);
}
