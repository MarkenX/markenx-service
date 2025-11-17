package com.udla.markenx.classroom.application.usecases.student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.classroom.application.dtos.requests.CreateStudentRequestDTO;
import com.udla.markenx.classroom.application.ports.out.auth.AuthenticationServicePort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.core.exceptions.DomainException;
import com.udla.markenx.classroom.core.models.Student;

@Service
public class CreateStudentUseCase {

  private final StudentRepositoryPort studentRepository;
  private final AuthenticationServicePort authenticationService;

  public CreateStudentUseCase(
      StudentRepositoryPort studentRepository,
      AuthenticationServicePort authenticationService) {
    this.studentRepository = studentRepository;
    this.authenticationService = authenticationService;
  }

  @Transactional
  public Student execute(CreateStudentRequestDTO request) {
    // Validate student doesn't exist in local database
    if (studentRepository.existsByEmail(request.email())) {
      throw new StudentAlreadyExistsException("A student with email " + request.email() + " already exists");
    }

    // Validate user doesn't exist in authentication system
    if (authenticationService.userExists(request.email())) {
      throw new StudentAlreadyExistsException(
          "A user with email " + request.email() + " already exists in authentication system");
    }

    String authUserId = null;
    try {
      // Create user in authentication system with STUDENT role
      authUserId = authenticationService.createUser(
          request.email(),
          request.password(),
          request.firstName(),
          request.lastName(),
          "STUDENT",
          true);

      // Create student in local database
      Student student = new Student(
          null,
          request.firstName(),
          request.lastName(),
          request.email());

      Student savedStudent = studentRepository.save(student, authUserId);

      return savedStudent;

    } catch (Exception e) {
      // Rollback: Delete from authentication system if local DB save failed
      if (authUserId != null) {
        try {
          authenticationService.deleteUser(authUserId);
        } catch (Exception rollbackException) {
          System.err.println("Failed to rollback authentication user creation: " + rollbackException.getMessage());
        }
      }
      throw new StudentCreationException("Failed to create student: " + e.getMessage(), e);
    }
  }

  public static class StudentAlreadyExistsException extends DomainException {
    public StudentAlreadyExistsException(String message) {
      super(message);
    }
  }

  public static class StudentCreationException extends DomainException {
    public StudentCreationException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
