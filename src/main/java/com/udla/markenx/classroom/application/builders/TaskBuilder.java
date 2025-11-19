package com.udla.markenx.classroom.application.builders;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.application.ports.out.data.generators.random.RandomAssignmentDataGeneratorPort;

@Component
public class TaskBuilder {

  private final RandomAssignmentDataGeneratorPort randomGenerator;

  private UUID courseId;
  private int academicTermYear;
  private String title;
  private String summary;
  private LocalDate dueDate;
  private int maxAttempts;
  private double minScoreToPass;
  private String createdBy;
  private boolean allowPastDate;

  public TaskBuilder(RandomAssignmentDataGeneratorPort randomGenerator) {
    this.randomGenerator = randomGenerator;
  }

  public TaskBuilder reset() {
    this.courseId = null;
    this.academicTermYear = -1;
    this.title = null;
    this.summary = null;
    this.dueDate = null;
    this.maxAttempts = -1;
    this.minScoreToPass = -1;
    this.createdBy = null;
    this.allowPastDate = false;
    return this;
  }

  public TaskBuilder setCourseId(UUID courseId) {
    this.courseId = courseId;
    return this;
  }

  public TaskBuilder setAcademicTermYear(int academicTermYear) {
    this.academicTermYear = academicTermYear;
    return this;
  }

  public TaskBuilder setTitle(String title) {
    this.title = title;
    return this;
  }

  public TaskBuilder setSummary(String summary) {
    this.summary = summary;
    return this;
  }

  public TaskBuilder setDueDate(LocalDate duedaDate) {
    this.dueDate = duedaDate;
    return this;
  }

  public TaskBuilder setMaxAttempts(int maxAttempts) {
    this.maxAttempts = maxAttempts;
    return this;
  }

  public TaskBuilder setMinScoreToPass(double minScoreToPass) {
    this.minScoreToPass = minScoreToPass;
    return this;
  }

  public TaskBuilder randomTitle() {
    this.title = randomGenerator.title();
    return this;
  }

  public TaskBuilder randomSummary() {
    this.summary = randomGenerator.summary();
    return this;
  }

  public TaskBuilder randomDueDate(LocalDate start, LocalDate end) {
    this.dueDate = randomGenerator.dueDate(start, end);
    return this;
  }

  public TaskBuilder randomMaxAttempts(int limit) {
    this.maxAttempts = randomGenerator.maxAttempt(limit);
    return this;
  }

  public TaskBuilder randomMinScoreToPass() {
    this.minScoreToPass = randomGenerator.minimumScoreToPass();
    return this;
  }

  public TaskBuilder setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  /**
   * Permite crear tareas con fechas en el pasado (para seeders/datos históricos).
   * Omite la validación de fecha futura.
   */
  public TaskBuilder allowPastDates() {
    this.allowPastDate = true;
    return this;
  }

  public Task build() {
    if (allowPastDate && createdBy != null) {
      return new Task(courseId, academicTermYear, title, summary, dueDate, maxAttempts, minScoreToPass, createdBy,
          true);
    }
    return new Task(courseId, academicTermYear, title, summary, dueDate, maxAttempts, minScoreToPass);
  }
}
