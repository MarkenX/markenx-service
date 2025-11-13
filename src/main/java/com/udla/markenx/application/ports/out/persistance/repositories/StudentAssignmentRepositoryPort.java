package com.udla.markenx.application.ports.out.persistance.repositories;

import com.udla.markenx.core.interfaces.StudentAssignment;

public interface StudentAssignmentRepositoryPort {
  StudentAssignment getByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

  StudentAssignment create(StudentAssignment studentAssignment);
}
