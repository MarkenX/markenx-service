package com.udla.markenx.classroom.application.services;

import com.udla.markenx.classroom.application.usecases.student.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.udla.markenx.classroom.application.dtos.requests.CreateStudentRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateStudentRequestDTO;
import com.udla.markenx.application.usecases.student.*;
import com.udla.markenx.classroom.core.models.Student;

/**
 * Application service (facade) for student management operations.
 * 
 * This service acts as a facade that orchestrates multiple use cases,
 * following the DDD pattern where controllers interact with services
 * rather than directly with use cases.
 * 
 * Benefits:
 * - Controllers remain thin and focused on HTTP concerns
 * - Use cases can be composed into complex workflows
 * - Easier to test and maintain
 * - Clear separation between application and infrastructure layers
 */
@Service
public class StudentManagementService {

  private final CreateStudentUseCase createStudentUseCase;
  private final GetAllStudentsUseCase getAllStudentsUseCase;
  private final GetStudentByIdUseCase getStudentByIdUseCase;
  private final UpdateStudentUseCase updateStudentUseCase;
  private final DeleteStudentUseCase deleteStudentUseCase;
  private final GetCurrentStudentProfileUseCase getCurrentStudentProfileUseCase;

  public StudentManagementService(
      CreateStudentUseCase createStudentUseCase,
      GetAllStudentsUseCase getAllStudentsUseCase,
      GetStudentByIdUseCase getStudentByIdUseCase,
      UpdateStudentUseCase updateStudentUseCase,
      DeleteStudentUseCase deleteStudentUseCase,
      GetCurrentStudentProfileUseCase getCurrentStudentProfileUseCase) {
    this.createStudentUseCase = createStudentUseCase;
    this.getAllStudentsUseCase = getAllStudentsUseCase;
    this.getStudentByIdUseCase = getStudentByIdUseCase;
    this.updateStudentUseCase = updateStudentUseCase;
    this.deleteStudentUseCase = deleteStudentUseCase;
    this.getCurrentStudentProfileUseCase = getCurrentStudentProfileUseCase;
  }

  /**
   * Creates a new student in both authentication system and database.
   * 
   * @param request student creation data
   * @return created student
   */
  public Student createStudent(CreateStudentRequestDTO request) {
    return createStudentUseCase.execute(request);
  }

  /**
   * Retrieves all students with pagination.
   * 
   * @param pageable pagination parameters
   * @return page of students
   */
  public Page<Student> getAllStudents(Pageable pageable) {
    return getAllStudentsUseCase.execute(pageable);
  }

  /**
   * Retrieves a student by their ID.
   * 
   * @param id student ID
   * @return student domain model
   */
  public Student getStudentById(Long id) {
    return getStudentByIdUseCase.execute(id);
  }

  /**
   * Updates an existing student.
   * 
   * @param id      student ID
   * @param request updated student data
   * @return updated student
   */
  public Student updateStudent(Long id, UpdateStudentRequestDTO request) {
    return updateStudentUseCase.execute(id, request);
  }

  /**
   * Deletes a student from database and authentication system.
   * 
   * @param id student ID
   */
  public void deleteStudent(Long id) {
    deleteStudentUseCase.execute(id);
  }

  /**
   * Retrieves the currently authenticated student's profile.
   * 
   * @param email email from JWT token
   * @return student profile
   */
  public Student getCurrentStudentProfile(String email) {
    return getCurrentStudentProfileUseCase.execute(email);
  }
}
