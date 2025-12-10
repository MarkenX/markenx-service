package com.udla.markenx.classroom.terms.application.services;

import com.udla.markenx.classroom.terms.application.commands.CreateCommand;
import com.udla.markenx.classroom.terms.application.ports.out.persistance.repositories.TermRepositoryPort;
import com.udla.markenx.classroom.terms.application.usecases.CreateTermUseCase;
import com.udla.markenx.classroom.terms.domain.model.Term;
import com.udla.markenx.classroom.terms.domain.model.TermFactory;
import com.udla.markenx.classroom.terms.domain.services.TermDomainService;
import com.udla.markenx.shared.domain.valueobjects.DateInterval;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateTermService implements CreateTermUseCase {

    private final TermRepositoryPort termRepository;

    @Override
    public Term execute(@NotNull CreateCommand command) {
        DateInterval dateInterval = createDateInterval(command);
        validateNoOverlappingTerms(dateInterval);
        int sequence = calculateNextSequence(command.year(), command.startDate());
        Term newTerm = buildTerm(command, dateInterval, sequence);
        return termRepository.save(newTerm);
    }

    private DateInterval createDateInterval(CreateCommand command) {
        return new DateInterval(command.startDate(), command.endDate());
    }

    private void validateNoOverlappingTerms(DateInterval dateInterval) {
        List<Term> overlappingTerms = termRepository.findOverlapping(dateInterval);
        TermDomainService.ensureNoOverlap(dateInterval, overlappingTerms);
    }

    private int calculateNextSequence(int year, LocalDate startDate) {
        int existingTermsCount = termRepository.countByYearBeforeDate(year, startDate);
        return existingTermsCount + 1;
    }

    private Term buildTerm(CreateCommand command, DateInterval interval, int sequence) {
        boolean isSingleYearTerm = interval.spansOneYear();

        if (command.isHistorical()) {
            return buildHistoricalTerm(interval, command.year(), sequence, isSingleYearTerm);
        }

        return buildNewTerm(interval, command.year(), sequence, command.createdBy(), isSingleYearTerm);
    }

    private Term buildHistoricalTerm(DateInterval interval, int year, int sequence, boolean isSingleYear) {
        return isSingleYear
                ? TermFactory.createSingleYearHistorical(interval, sequence)
                : TermFactory.createCrossYearHistorical(interval, year, sequence);
    }

    private Term buildNewTerm(DateInterval interval, int year, int sequence,
                              String createdBy, boolean isSingleYear) {
        return isSingleYear
                ? TermFactory.createSingleYearNew(interval, sequence, createdBy)
                : TermFactory.createCrossYearNew(interval, year, sequence, createdBy);
    }
}