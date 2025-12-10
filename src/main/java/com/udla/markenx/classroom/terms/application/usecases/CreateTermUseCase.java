package com.udla.markenx.classroom.terms.application.usecases;

import com.udla.markenx.classroom.terms.application.commands.CreateCommand;
import com.udla.markenx.classroom.terms.domain.model.Term;

public interface CreateTermUseCase {
    Term execute(CreateCommand cmd);
}
