package com.udla.markenx.core.models;

import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.core.interfaces.Assignment;

import lombok.Getter;

@Getter
public class Course {

  private Long id;
  private final List<Assignment> assignments;
  private final List<Student> students;

  public Course() {
    this.assignments = new ArrayList<>();
    this.students = new ArrayList<>();
  }

  public Course(long id) {
    this.id = id;
    this.assignments = new ArrayList<>();
    this.students = new ArrayList<>();
  }

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
