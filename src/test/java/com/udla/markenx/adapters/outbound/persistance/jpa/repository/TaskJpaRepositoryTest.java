package com.udla.markenx.adapters.outbound.persistance.jpa.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.udla.markenx.core.enums.AssignmentStatus;
import com.udla.markenx.infrastructure.out.repository.jpa.entity.StudentJpaEntity;
import com.udla.markenx.infrastructure.out.repository.jpa.entity.TaskJpaEntity;
import com.udla.markenx.infrastructure.out.repository.jpa.repository.StudentJpaRepository;
import com.udla.markenx.infrastructure.out.repository.jpa.repository.TaskJpaRepository;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Transactional
@DisplayName("Tests for TaskJpaRepository")
class TaskJpaRepositoryTest {

        private static final String TASK_TITLE = "Title";
        private static final String TASK_SUMMARY = "Summary";
        private static final AssignmentStatus TASK_STATUS = AssignmentStatus.NOT_STARTED;
        private static final LocalDate DUE_DATE = LocalDate.now().plusDays(5);
        private static final int TASK_MAX_ATTEMPTS = 3;
        private static final int TASK_ACTIVE_ATTEMPT = 0;

        @Autowired
        private TaskJpaRepository taskJpaRepository;

        @Autowired
        private StudentJpaRepository studentJpaRepository;

        private StudentJpaEntity student;

        @BeforeEach
        void setUp() {
                student = studentJpaRepository.save(new StudentJpaEntity("John", "Doe"));
                taskJpaRepository.save(
                                new TaskJpaEntity(TASK_TITLE, TASK_SUMMARY, TASK_STATUS, DUE_DATE, TASK_ACTIVE_ATTEMPT,
                                                TASK_MAX_ATTEMPTS, student));
        }

        @Test
        @DisplayName("Returns non-null page when searching by student ID and NOT_STARTED status")
        void shouldReturnNonNullPageWhenSearchingByStudentIdAndNotStartedStatus() {
                Page<TaskJpaEntity> result = taskJpaRepository.findByStudentIdAndCurrentStatus(
                                student.getId(),
                                TASK_STATUS,
                                PageRequest.of(0, 10));

                assertThat("Result page should not be null", result, is(notNullValue()));
        }

        @Test
        @DisplayName("Returns page with one task when searching by student ID and NOT_STARTED status")
        void shouldReturnPageWithOneTaskWhenSearchingByStudentIdAndNotStartedStatus() {
                Page<TaskJpaEntity> result = taskJpaRepository.findByStudentIdAndCurrentStatus(
                                student.getId(),
                                TASK_STATUS,
                                PageRequest.of(0, 10));

                assertThat("Page should contain exactly one task", result.getContent(), hasSize(1));
        }

        @Test
        @DisplayName("Returns task with correct title when searching by student ID and NOT_STARTED status")
        void shouldReturnTaskWithCorrectTitleWhenSearchingByStudentIdAndNotStartedStatus() {
                Page<TaskJpaEntity> result = taskJpaRepository.findByStudentIdAndCurrentStatus(
                                student.getId(),
                                TASK_STATUS,
                                PageRequest.of(0, 10));

                assertThat("Task title should match", result.getContent().get(0).getTitle(), equalTo(TASK_TITLE));
        }

        @Test
        @DisplayName("Returns task with correct summary when searching by student ID and NOT_STARTED status")
        void shouldReturnTaskWithCorrectSummaryWhenSearchingByStudentIdAndNotStartedStatus() {
                Page<TaskJpaEntity> result = taskJpaRepository.findByStudentIdAndCurrentStatus(
                                student.getId(),
                                TASK_STATUS,
                                PageRequest.of(0, 10));

                assertThat("Task summary should match", result.getContent().get(0).getSummary(),
                                equalTo(TASK_SUMMARY));
        }

        @Test
        @DisplayName("Returns task with correct status when searching by student ID and NOT_STARTED status")
        void shouldReturnTaskWithCorrectStatusWhenSearchingByStudentIdAndNotStartedStatus() {
                Page<TaskJpaEntity> result = taskJpaRepository.findByStudentIdAndCurrentStatus(
                                student.getId(),
                                TASK_STATUS,
                                PageRequest.of(0, 10));

                assertThat("Task status should match", result.getContent().get(0).getCurrentStatus(),
                                equalTo(TASK_STATUS));
        }

        @Test
        @DisplayName("Returns task with correct due date when searching by student ID and NOT_STARTED status")
        void shouldReturnTaskWithCorrectDueDateWhenSearchingByStudentIdAndNotStartedStatus() {
                Page<TaskJpaEntity> result = taskJpaRepository.findByStudentIdAndCurrentStatus(
                                student.getId(),
                                TASK_STATUS,
                                PageRequest.of(0, 10));

                assertThat("Task due date should match", result.getContent().get(0).getDueDate(), equalTo(DUE_DATE));
        }

        @Test
        @DisplayName("Returns task with correct active attempt when searching by student ID and NOT_STARTED status")
        void shouldReturnTaskWithCorrectProgressWhenSearchingByStudentIdAndNotStartedStatus() {
                Page<TaskJpaEntity> result = taskJpaRepository.findByStudentIdAndCurrentStatus(
                                student.getId(),
                                TASK_STATUS,
                                PageRequest.of(0, 10));

                assertThat("Task active attempt should match", result.getContent().get(0).getActiveAttempt(),
                                equalTo(TASK_ACTIVE_ATTEMPT));
        }

        @Test
        @DisplayName("Returns task with correct max attempts when searching by student ID and NOT_STARTED status")
        void shouldReturnTaskWithCorrectPriorityWhenSearchingByStudentIdAndNotStartedStatus() {
                Page<TaskJpaEntity> result = taskJpaRepository.findByStudentIdAndCurrentStatus(
                                student.getId(),
                                TASK_STATUS,
                                PageRequest.of(0, 10));

                assertThat("Task max attempts should match", result.getContent().get(0).getMaxAttempts(),
                                equalTo(TASK_MAX_ATTEMPTS));
        }

        @Test
        @DisplayName("Returns task with correct student ID when searching by student ID and NOT_STARTED status")
        void shouldReturnTaskWithCorrectStudentWhenSearchingByStudentIdAndNotStartedStatus() {
                Page<TaskJpaEntity> result = taskJpaRepository.findByStudentIdAndCurrentStatus(
                                student.getId(),
                                TASK_STATUS,
                                PageRequest.of(0, 10));

                assertThat("Task student ID should match", result.getContent().get(0).getStudent().getId(),
                                equalTo(student.getId()));
        }
}