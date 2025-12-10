package com.udla.markenx.classroom.terms.application.services;

import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.terms.application.usecases.CreateTermUseCase;
import com.udla.markenx.classroom.terms.domain.model.Term;
import com.udla.markenx.classroom.terms.application.commands.CreateCommand;
import com.udla.markenx.classroom.terms.application.usecases.ManageTermsUseCase;
import com.udla.markenx.classroom.terms.application.ports.out.persistance.repositories.TermRepositoryPort;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ManageTermsService implements ManageTermsUseCase {

    private final TermRepositoryPort repository;
    private final CreateTermUseCase createTermUseCase;

    public ManageTermsService(
            TermRepositoryPort repository,
            @Qualifier("CreateTerm") CreateTermUseCase createTermUseCase) {
        this.repository = repository;
        this.createTermUseCase = createTermUseCase;
    }

    @Override
    public Page<Term> getAllAcademicTerms(Pageable pageable) {
        return repository.findAllPaged(pageable);
    }

    @Override
    public Term createTerm(CreateCommand cmd) {
        return createTermUseCase.execute(cmd);
    }

    @Override
    @Transactional(readOnly = true)
    public Term getAcademicTermById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Período académico", id));
    }
}

