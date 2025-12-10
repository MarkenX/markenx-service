package com.udla.markenx.classroom.terms.domain.model;

import java.time.LocalDate;

import com.udla.markenx.shared.domain.valueobjects.DateInterval;
import com.udla.markenx.shared.domain.model.DomainBaseModel;
import com.udla.markenx.shared.domain.valueobjects.AuditParams;
import com.udla.markenx.shared.domain.valueobjects.EntityInfo;

import com.udla.markenx.classroom.terms.domain.valueobjects.TermStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class Term extends DomainBaseModel {

    private final TermStatus status;
    private final int sequence;
    private final int year;

    @Getter(AccessLevel.NONE)
    private final DateInterval dateInterval;

    protected Term(DateInterval dateInterval, int year, int sequence, TermStatus status) {
        super();
        this.dateInterval = dateInterval;
        this.year = year;
        this.sequence = sequence;
        this.status = status;
    }

    protected Term(DateInterval dateInterval, int year, int sequence, TermStatus status, String createdBy) {
        super(createdBy);
        this.dateInterval = dateInterval;
        this.year = year;
        this.sequence = sequence;
        this.status = status;
    }

    protected Term(EntityInfo entityInfo, DateInterval dateInterval, int year, int sequence, TermStatus status, AuditParams auditParams) {
        super(entityInfo.id(), entityInfo.status(), auditParams);
        this.dateInterval = dateInterval;
        this.year = year;
        this.sequence = sequence;
        this.status = status;
    }

    public LocalDate getStartDate() {
        return this.dateInterval.startDate();
    }

    public LocalDate getEndDate() {
        return this.dateInterval.endDate();
    }

    public boolean overlapsWith(@NotNull Term term) {
        return dateInterval.overlapsWith(term.dateInterval);
    }

    private TermStatus updateStatus(LocalDate termStartDate, LocalDate termEndDate) {
        LocalDate today = LocalDate.now();
        if (today.isAfter(termStartDate) && today.isBefore(termEndDate)) {
            return TermStatus.ACTIVE;
        }
        if (today.isBefore(termStartDate)) {
            return TermStatus.UPCOMING;
        }
        return TermStatus.ENDED;
    }
}
