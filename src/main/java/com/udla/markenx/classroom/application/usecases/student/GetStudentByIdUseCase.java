package com.udla.markenx.classroom.application.usecases.student;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.DomainException;
import com.udla.markenx.classroom.domain.models.Student;

@Service
public class GetStudentByIdUseCase {

  private final StudentRepositoryPort studentRepository;

  public GetStudentByIdUseCase(StudentRepositoryPort studentRepository) {
    this.studentRepository = studentRepository;
  }

  public Student execute(Long id) {
    if (isAdmin()) {
      return studentRepository.findByIdIncludingDisabled(id)
          .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
    }
    return studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }

  public static class StudentNotFoundException extends DomainException {
    public StudentNotFoundException(String message) {
      super(message);
    }
  }
}
