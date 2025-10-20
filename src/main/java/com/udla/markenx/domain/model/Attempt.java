package com.udla.markenx.domain.model;

import java.time.Duration;
import java.util.Date;

public class Attempt {
    private double score;
    private Date date;
    private Duration duration;

    public Attempt(double score, Date date, Duration duration) {
        this.score = score;
        this.date = date;
        this.duration = duration;
    }

    //#region Getters

    public double getScore() {
        return score;
    } 

    public Date getDate() {
        return date;
    }

    public Duration getDuration() {
        return duration;
    }

    //#endregion
}
