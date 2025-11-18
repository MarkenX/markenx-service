package com.udla.markenx.classroom.application.usecases.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.domain.models.Student;

@Service
public class GetAllStudentsUseCase {

  private final StudentRepositoryPort studentRepository;

  public GetAllStudentsUseCase(StudentRepositoryPort studentRepository) {
    this.studentRepository = studentRepository;
  }

  public Page<Student> execute(Pageable pageable) {
    if (isAdmin()) {
      return studentRepository.findAllIncludingDisabled(pageable);
    }
    return studentRepository.findAll(pageable);
  }

  private boolean isAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
  }
}
