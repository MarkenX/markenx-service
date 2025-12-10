package com.udla.markenx.classroom.terms.application.usecases;

import com.udla.markenx.classroom.terms.application.commands.CreateCommand;
import com.udla.markenx.classroom.terms.domain.model.Term;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface ManageTermsUseCase {
    Page<Term> getAllAcademicTerms(Pageable pageable);
    Term createTerm(CreateCommand cmd);
    Term getAcademicTermById(UUID id);
}
