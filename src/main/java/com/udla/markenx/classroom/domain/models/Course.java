package com.udla.markenx.classroom.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.udla.markenx.classroom.domain.interfaces.Assignment;
import com.udla.markenx.shared.domain.model.DomainBaseModel;
import com.udla.markenx.shared.domain.util.validator.EntityValidator;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

public class Course extends DomainBaseModel {
  private static final Class<Course> CLAZZ = Course.class;
  private static final String PREFIX = "CRS";

  private String name;
  private final String code;
  private final Long sequence;
  private final UUID academicTermId;
  private final int academicTermYear;
  private final List<Student> students;
  private final List<Assignment> assignments;

  public Course(
      UUID id,
      String code,
      Long serialNumber,
      DomainBaseModelStatus status,
      UUID academicTermId,
      int academicTermYear,
      String name,
      List<Student> students,
      List<? extends Assignment> assignments,
      String createdBy,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super(id, code, status, createdBy, createdAt, updatedAt);
    this.sequence = serialNumber;
    this.academicTermId = academicTermId;
    this.academicTermYear = academicTermYear;
    setName(name);
    this.students = requireStudents(students);
    this.assignments = new ArrayList<>(requireAssignments(assignments));
    this.code = requireCode(code);
  }

  public Course(UUID academicTermId, int academicTermYear, String name, String createdBy) {
    super(createdBy);
    this.sequence = null;
    this.academicTermId = academicTermId;
    this.academicTermYear = academicTermYear;
    setName(name);
    this.students = new ArrayList<>();
    this.assignments = new ArrayList<>();
    this.code = generateCode();
  }

  public Course(UUID academicTermId, int academicTermYear, String name) {
    super();
    this.sequence = null;
    this.academicTermId = academicTermId;
    this.academicTermYear = academicTermYear;
    setName(name);
    this.students = new ArrayList<>();
    this.assignments = new ArrayList<>();
    this.code = generateCode();
  }

  public String getCode() {
    return this.code;
  }

  public UUID getAcademicTermId() {
    return this.academicTermId;
  }

  public String getName() {
    return this.name;
  }

  public Long getSequence() {
    return this.sequence;
  }

  public List<Student> getStudents() {
    return List.copyOf(this.students);
  }

  public List<Assignment> getAssignments() {
    return List.copyOf(this.assignments);
  }

  public void setName(String name) {
    this.name = requireName(name);
  }

  private String requireName(String name) {
    return EntityValidator.ensureNotNullOrEmpty(CLAZZ, name, "name");
  }

  private List<? extends Assignment> requireAssignments(List<? extends Assignment> assignments) {
    return EntityValidator.ensureNotNull(CLAZZ, assignments, "assignments");
  }

  private List<Student> requireStudents(List<Student> students) {
    return EntityValidator.ensureNotNull(CLAZZ, students, "students");
  }

  private Student requireStudent(Student student) {
    return EntityValidator.ensureNotNull(CLAZZ, student, "student");
  }

  private Assignment requireAssignment(Assignment assignment) {
    return EntityValidator.ensureNotNull(CLAZZ, assignment, "assignment");
  }

  public boolean addAssignment(Assignment assignment) {
    return this.assignments.add(requireAssignment(assignment));
  }

  public void addAssignments(List<? extends Assignment> assignments) {
    this.assignments.addAll(requireAssignments(assignments));
  }

  public boolean addStudent(Student student) {
    return this.students.add(requireStudent(student));
  }

  public void addStudents(List<Student> students) {
    this.students.addAll(requireStudents(students));
  }

  @Override
  protected String generateCode() {
    return String.format("%s-%d-%04d", PREFIX, academicTermYear, sequence);
  }
}
