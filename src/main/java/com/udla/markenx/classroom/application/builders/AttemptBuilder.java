package com.udla.markenx.classroom.application.builders;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomAttemptDataGeneratorPort;
import com.udla.markenx.classroom.core.models.Attempt;
import com.udla.markenx.classroom.core.valueobjects.enums.AttemptStatus;

@Component
public class AttemptBuilder {

  private final RandomAttemptDataGeneratorPort randomGenerator;

  private UUID studentTaskId;
  private Long studentSequence;
  private Long taskSequence;
  private double taskMinScoreToPass;
  private double score;
  private Duration timeSpent;
  private AttemptStatus attemptStatus;

  public AttemptBuilder reset() {
    this.studentTaskId = null;
    this.studentSequence = null;
    this.taskSequence = null;
    this.taskMinScoreToPass = -1;
    this.score = -1;
    this.timeSpent = null;
    this.attemptStatus = null;
    return this;
  }

  public AttemptBuilder(RandomAttemptDataGeneratorPort randomGenerator) {
    this.randomGenerator = randomGenerator;
  }

  public AttemptBuilder setStudentSequence(Long studentSequence) {
    this.studentSequence = studentSequence;
    return this;
  }

  public AttemptBuilder setTaskSequence(Long taskSequence) {
    this.taskSequence = taskSequence;
    return this;
  }

  public AttemptBuilder setTaskMinSocreToPass(double taskMinScoreToPass) {
    this.taskMinScoreToPass = taskMinScoreToPass;
    return this;
  }

  public AttemptBuilder setStudentTaskId(UUID studentTaskId) {
    this.studentTaskId = studentTaskId;
    return this;
  }

  public AttemptBuilder setScore(double score) {
    this.score = score;
    return this;
  }

  public AttemptBuilder setTimeSpent(Duration timeSpent) {
    this.timeSpent = timeSpent;
    return this;
  }

  public AttemptBuilder setAttemptStatus(AttemptStatus attemptStatus) {
    this.attemptStatus = attemptStatus;
    return this;
  }

  public AttemptBuilder randomScore() {
    this.score = randomGenerator.score();
    return this;
  }

  public AttemptBuilder randomDuration() {
    this.timeSpent = randomGenerator.duration();
    return this;
  }

  public Attempt build() {
    return new Attempt(studentSequence, taskSequence, taskMinScoreToPass, score, timeSpent, attemptStatus,
        studentTaskId);
  }
}
