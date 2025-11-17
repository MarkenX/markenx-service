package com.udla.markenx.classroom.application.ports.out.persistance.repositories;

import com.udla.markenx.classroom.core.interfaces.StudentAssignment;

public interface StudentAssignmentRepositoryPort {
  StudentAssignment getByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

  StudentAssignment create(StudentAssignment studentAssignment);
}
