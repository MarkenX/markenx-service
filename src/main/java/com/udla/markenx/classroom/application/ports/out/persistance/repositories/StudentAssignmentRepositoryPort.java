package com.udla.markenx.classroom.application.ports.out.persistance.repositories;

import com.udla.markenx.classroom.domain.interfaces.Assignment;
import com.udla.markenx.classroom.domain.interfaces.StudentAssignment;

public interface StudentAssignmentRepositoryPort {
  StudentAssignment<? extends Assignment> getByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

  StudentAssignment<? extends Assignment> create(StudentAssignment<? extends Assignment> studentAssignment);
}
