package com.udla.markenx.core.models;

import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.core.exceptions.NullFieldException;
import com.udla.markenx.core.interfaces.Assignment;

import lombok.Getter;

@Getter
public class Course {
  private final Long id;
  private final List<Assignment> assignments;
  private final List<Student> students;

  // #region Constructors

  public Course() {
    this.id = null;
    this.assignments = new ArrayList<>();
    this.students = new ArrayList<>();
  }

  public Course(Long id, List<Assignment> assignments, List<Student> students) {
    this.id = ensureValidId(id);
    this.assignments = ensureValidAssignments(assignments);
    this.students = ensureValidStudents(students);
  }

  // #endregion Constructors

  // #region Validations

  private long ensureValidId(Long id) {
    if (id == null) {
      throw new NullFieldException("Course", "id");
    }
    return id;
  }

  private List<Assignment> ensureValidAssignments(List<Assignment> assignments) {
    if (assignments == null) {
      throw new NullFieldException("Course", "assignments");
    }
    return assignments;
  }

  private List<Student> ensureValidStudents(List<Student> students) {
    if (students == null) {
      throw new NullFieldException("Course", "students");
    }
    return students;
  }

  // #endregion Validations

  public boolean addAssignment(Assignment assignment) {
    if (assignment == null) {
      return false;
    }
    return this.assignments.add(assignment);
  }

  public boolean addStudent(Student student) {
    if (student == null) {
      return false;
    }
    return this.students.add(student);
  }
}
