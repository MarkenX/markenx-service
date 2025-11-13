package com.udla.markenx.core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.core.interfaces.Assignment;
import com.udla.markenx.core.utils.validators.EntityValidator;
import com.udla.markenx.core.valueobjects.AuditInfo;

public class Course {
  private static final Class<Course> CLAZZ = Course.class;

  private final Long id;
  private final Long academicTermId;
  private String name;
  private final List<Student> students;
  private final List<Assignment> assignments;
  private final AuditInfo auditInfo;

  private Course(Long id, Long academicTermId, String name, List<Student> students,
      List<Assignment> assignments, AuditInfo auditInfo) {
    this.id = id;
    this.academicTermId = academicTermId;
    this.name = requireName(name);
    this.students = requireStudents(students);
    this.assignments = requireAssignments(assignments);
    this.auditInfo = requireAuditInfo(auditInfo);
  }

  public Course(Long id, Long academicTermId, String name, List<Student> students,
      List<Assignment> assignments,
      String createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this(id, academicTermId, name, students, assignments, new AuditInfo(createdBy, createdAt, updatedAt));
  }

  public Course(String name) {
    this(null, null, name, new ArrayList<>(), new ArrayList<>(), new AuditInfo());
  }

  public Long getId() {
    return this.id;
  }

  public Long getAcademicTermId() {
    return this.academicTermId;
  }

  public String getName() {
    return this.name;
  }

  public List<Student> getStudents() {
    return List.copyOf(this.students);
  }

  public List<Assignment> getAssignments() {
    return List.copyOf(this.assignments);
  }

  public String getCreatedBy() {
    return this.auditInfo.getCreatedBy();
  }

  public LocalDateTime getCreatedAt() {
    return this.auditInfo.getCreatedDateTime();
  }

  public LocalDateTime getUpdatedAt() {
    return this.auditInfo.getUpdatedDateTime();
  }

  public void setName(String name) {
    this.name = requireName(name);
  }

  private String requireName(String name) {
    return EntityValidator.ensureNotNullOrEmpty(CLAZZ, name, "name");
  }

  private List<Assignment> requireAssignments(List<Assignment> assignments) {
    return EntityValidator.ensureNotNull(CLAZZ, assignments, "assignments");
  }

  private List<Student> requireStudents(List<Student> students) {
    return EntityValidator.ensureNotNull(CLAZZ, students, "students");
  }

  private Student requireStudent(Student student) {
    return EntityValidator.ensureNotNull(CLAZZ, student, "student");
  }

  private AuditInfo requireAuditInfo(AuditInfo auditInfo) {
    return EntityValidator.ensureNotNull(CLAZZ, auditInfo, "auditInfo");
  }

  private Assignment requireAssignment(Assignment assignment) {
    return EntityValidator.ensureNotNull(CLAZZ, assignment, "assignment");
  }

  public boolean addAssignment(Assignment assignment) {
    return this.assignments.add(requireAssignment(assignment));
  }

  public void addAssignments(List<Assignment> assignments) {
    this.assignments.addAll(requireAssignments(assignments));
  }

  public boolean addStudent(Student student) {
    return this.students.add(requireStudent(student));
  }

  public void addStudents(List<Student> students) {
    this.students.addAll(requireStudents(students));
  }

  public void markUpdated() {
    this.auditInfo.markUpdated();
  }
}
