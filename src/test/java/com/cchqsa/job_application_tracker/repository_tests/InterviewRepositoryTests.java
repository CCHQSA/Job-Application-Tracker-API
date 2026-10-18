package com.cchqsa.job_application_tracker.repository_tests;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.Interview;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.enums.Currency;
import com.cchqsa.job_application_tracker.enums.InterviewStatus;
import com.cchqsa.job_application_tracker.enums.InterviewType;
import com.cchqsa.job_application_tracker.repository.InterviewRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class InterviewRepositoryTests {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void save_persistsInterviewAndGeneratedId() {
        Application application = persistApplication("owner@example.com");
        Interview interview = createInterview(application, LocalDateTime.of(2026, 10, 23, 12, 0));

        Interview saved = interviewRepository.save(interview);
        entityManager.flush();
        Long interviewId = saved.getId();
        entityManager.clear();

        Interview reloaded = interviewRepository.findById(interviewId).orElseThrow();
        Assertions.assertTrue(interviewId > 0);
        assertInterviewFields(interview, reloaded);
    }

    @Test
    void findByApplicationIn_returnsInterviewsForRequestedApplications() {
        Application firstApplication = persistApplication("first@example.com");
        Application secondApplication = persistApplication("second@example.com");
        Application excludedApplication = persistApplication("excluded@example.com");
        persistInterview(firstApplication, LocalDateTime.now().plusDays(1));
        persistInterview(firstApplication, LocalDateTime.now().plusDays(2));
        persistInterview(secondApplication, LocalDateTime.now().plusDays(3));
        persistInterview(excludedApplication, LocalDateTime.now().plusDays(4));
        entityManager.flush();
        entityManager.clear();

        List<Interview> found = interviewRepository.findByApplicationIn(
                List.of(firstApplication, secondApplication));

        Assertions.assertEquals(3, found.size());
        Assertions.assertTrue(found.stream().allMatch(interview ->
                List.of(firstApplication.getId(), secondApplication.getId())
                        .contains(interview.getApplication().getId())));
    }

    @Test
    void findByApplicationInOrderByDateTimeDesc_returnsNewestFirst() {
        Application application = persistApplication("owner@example.com");
        Interview older = persistInterview(application, LocalDateTime.of(2026, 1, 1, 10, 0));
        Interview newer = persistInterview(application, LocalDateTime.of(2026, 1, 3, 10, 0));
        Interview middle = persistInterview(application, LocalDateTime.of(2026, 1, 2, 10, 0));
        entityManager.flush();
        entityManager.clear();

        List<Interview> found = interviewRepository
                .findByApplicationInOrderByDateTimeDesc(List.of(application));

        Assertions.assertEquals(List.of(newer.getId(), middle.getId(), older.getId()),
                found.stream().map(Interview::getId).toList());
    }

    @Test
    void findByApplicationInWithApplication_fetchesApplicationAndUserNewestFirst() {
        Application application = persistApplication("owner@example.com");
        persistInterview(application, LocalDateTime.of(2026, 1, 1, 10, 0));
        persistInterview(application, LocalDateTime.of(2026, 1, 2, 10, 0));
        entityManager.flush();
        entityManager.clear();

        List<Interview> found = interviewRepository
                .findByApplicationInWithApplication(List.of(application));

        Assertions.assertEquals(2, found.size());
        Assertions.assertTrue(found.get(0).getDateTime().isAfter(found.get(1).getDateTime()));
        Assertions.assertTrue(Hibernate.isInitialized(found.get(0).getApplication()));
        Assertions.assertTrue(Hibernate.isInitialized(found.get(0).getApplication().getUser()));
        Assertions.assertEquals("owner@example.com", found.get(0).getApplication().getUser().getEmail());
    }

    @Test
    void findByIdWithApplicationAndUser_returnsInterviewWithRelatedEntitiesFetched() {
        Application application = persistApplication("owner@example.com");
        Interview interview = persistInterview(application, LocalDateTime.now().plusDays(5));
        entityManager.flush();
        Long interviewId = interview.getId();
        entityManager.clear();

        Interview found = interviewRepository.findByIdWithApplicationAndUser(interviewId).orElseThrow();

        Assertions.assertEquals(interviewId, found.getId());
        Assertions.assertTrue(Hibernate.isInitialized(found.getApplication()));
        Assertions.assertTrue(Hibernate.isInitialized(found.getApplication().getUser()));
        Assertions.assertEquals(application.getId(), found.getApplication().getId());
        Assertions.assertEquals("owner@example.com", found.getApplication().getUser().getEmail());
    }

    @Test
    void findByIdWithApplicationAndUser_returnsEmptyForUnknownId() {
        Assertions.assertTrue(interviewRepository.findByIdWithApplicationAndUser(-1L).isEmpty());
    }

    @Test
    void deleteAllByApplicationIn_removesOnlyInterviewsForRequestedApplications() {
        Application targetApplication = persistApplication("target@example.com");
        Application otherApplication = persistApplication("other@example.com");
        Interview firstTargetInterview = persistInterview(targetApplication, LocalDateTime.now().plusDays(1));
        Interview secondTargetInterview = persistInterview(targetApplication, LocalDateTime.now().plusDays(2));
        Interview otherInterview = persistInterview(otherApplication, LocalDateTime.now().plusDays(3));
        entityManager.flush();

        interviewRepository.deleteAllByApplicationIn(List.of(targetApplication));
        entityManager.flush();
        entityManager.clear();

        Assertions.assertTrue(interviewRepository.findById(firstTargetInterview.getId()).isEmpty());
        Assertions.assertTrue(interviewRepository.findById(secondTargetInterview.getId()).isEmpty());
        Assertions.assertTrue(interviewRepository.findById(otherInterview.getId()).isPresent());
    }

    private Application persistApplication(String email) {
        User user = User.builder()
                .email(email)
                .name("Test")
                .lastName("User")
                .password("Pass22042010!")
                .build();
        entityManager.persist(user);

        Application application = Application.builder()
                .company("Example Inc.")
                .position("Engineer")
                .location("Remote")
                .salaryFrom(BigDecimal.valueOf(100))
                .salaryTo(BigDecimal.valueOf(300))
                .currency(Currency.USD)
                .applicationDate(LocalDate.now())
                .user(user)
                .build();
        entityManager.persist(application);
        entityManager.flush();
        return application;
    }

    private Interview persistInterview(Application application, LocalDateTime dateTime) {
        Interview interview = createInterview(application, dateTime);
        entityManager.persist(interview);
        return interview;
    }

    private Interview createInterview(Application application, LocalDateTime dateTime) {
        return Interview.builder()
                .application(application)
                .type(InterviewType.HR)
                .dateTime(dateTime)
                .location("Remote")
                .meetingUrl("https://example.com/meeting")
                .interviewer("HR interviewer")
                .notes("Repository test interview")
                .status(InterviewStatus.SCHEDULED)
                .build();
    }

    private void assertInterviewFields(Interview expected, Interview actual) {
        Assertions.assertEquals(expected.getType(), actual.getType());
        Assertions.assertEquals(expected.getDateTime(), actual.getDateTime());
        Assertions.assertEquals(expected.getLocation(), actual.getLocation());
        Assertions.assertEquals(expected.getMeetingUrl(), actual.getMeetingUrl());
        Assertions.assertEquals(expected.getInterviewer(), actual.getInterviewer());
        Assertions.assertEquals(expected.getNotes(), actual.getNotes());
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
        Assertions.assertNotNull(actual.getCreatedAt());
        Assertions.assertNotNull(actual.getUpdatedAt());
        Assertions.assertEquals(expected.getApplication().getId(), actual.getApplication().getId());
    }
}
