package com.udla.markenx.classroom.terms.domain.services;

import com.udla.markenx.classroom.terms.domain.model.Term;
import com.udla.markenx.shared.domain.valueobjects.DateInterval;

import java.util.List;

public class TermDomainService {

    public static void ensureNoOverlap(DateInterval interval, List<Term> existingTerms) {
        boolean overlaps = existingTerms.stream()
                .anyMatch(term -> term.overlapsWith(term));

        if (overlaps) {
            throw new IllegalArgumentException("A term already exists in this interval");
        }
    }
}
