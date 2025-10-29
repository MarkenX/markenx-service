package com.udla.markenx.domain.port.out;

import org.springframework.data.domain.Page;

import com.udla.markenx.domain.model.Attempt;

public interface AttemptRepositoryPort {
    Page<Attempt> getAttemptsByTaskId(Long taskId, int page, int size);
}
