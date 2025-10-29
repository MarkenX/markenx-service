package com.udla.markenx.domain.model;

import java.time.Duration;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attempt {
    private Long id;
    private double score;
    private LocalDate date;
    private Duration duration;

    public Attempt(
            double score,
            LocalDate date,
            Duration duration
            ) {
        this.score = score;
        this.date = date;
        this.duration = duration;
    }
}
