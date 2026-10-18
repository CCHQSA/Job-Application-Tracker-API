package com.cchqsa.job_application_tracker.repository_tests;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.enums.ApplicationStatus;
import com.cchqsa.job_application_tracker.enums.Currency;
import com.cchqsa.job_application_tracker.repository.ApplicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ApplicationRepositoryTests {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User createTestUser() {
        return User.builder()
                .email("testemail@gmail.com")
                .name("Name").lastName("lastName")
                .password("Pass22042010!")
                .build();
    }

    @Test
    void save_persistsApplicationAndGeneratedId() {
        User user = createTestUser();
        entityManager.persistAndFlush(user);

        Application application = createApplication(user);

        Application savedApplication = applicationRepository.save(application);
        entityManager.flush();
        Long savedId = savedApplication.getId();
        entityManager.clear();

        Application reloaded = applicationRepository.findById(savedId).orElseThrow();

        Assertions.assertTrue(savedId > 0);
        assertApplicationFields(application, reloaded);
    }

    @Test
    void findById_returnsPersistedApplication() {
        User user = createTestUser();
        entityManager.persistAndFlush(user);

        Application application = createApplication(user);
        entityManager.persistAndFlush(application);
        entityManager.clear();

        Application found = applicationRepository.findById(application.getId()).orElseThrow();

        assertApplicationFields(application, found);
    }

    @Test
    void findByIdAndUser_returnsApplicationOwnedByUser() {
        User user = createTestUser();
        entityManager.persist(user);

        Application application = createApplication(user);
        applicationRepository.save(application);
        entityManager.flush();

        Application found = applicationRepository
                .findByIdAndUser(application.getId(), user)
                .orElseThrow();
        entityManager.clear();

        assertApplicationFields(application, found);
    }

    @Test
    void findByIdAndUser_returnsEmptyForDifferentUser() {
        User owner = createTestUser();
        User otherUser = User.builder()
                .email("other@example.com")
                .name("Other")
                .lastName("User")
                .password("Pass22042010!")
                .build();
        entityManager.persistAndFlush(owner);
        entityManager.persistAndFlush(otherUser);

        Application application = createApplication(owner);
        entityManager.persistAndFlush(application);

        Assertions.assertTrue(applicationRepository
                .findByIdAndUser(application.getId(), otherUser)
                .isEmpty());
    }

    @Test
    void getApplicationsByUser_returnsOnlyThatUsersApplications() {
        User owner = createTestUser();
        User otherUser = User.builder()
                .email("other@example.com")
                .name("Other")
                .lastName("User")
                .password("Pass22042010!")
                .build();
        entityManager.persistAndFlush(owner);
        entityManager.persistAndFlush(otherUser);

        entityManager.persist(createApplication(owner));
        entityManager.persist(createApplication(owner));
        entityManager.persist(createApplication(otherUser));
        entityManager.flush();

        var applications = applicationRepository.getApplicationsByUser(owner);

        Assertions.assertEquals(2, applications.size());
        Assertions.assertTrue(applications.stream()
                .allMatch(application -> application.getUser().getId().equals(owner.getId())));
    }

    @Test
    void delete_removesApplication() {
        User user = createTestUser();
        entityManager.persist(user);
        Application application = createApplication(user);
        applicationRepository.save(application);
        applicationRepository.delete(application);

        entityManager.flush();
        entityManager.clear();

        Assertions.assertTrue(applicationRepository.findById(application.getId()).isEmpty());
    }

    @Test
    void deleteAllByUser_removesOnlyThatUsersApplications() {
        User user = createTestUser();
        User otherUser = User.builder()
                .email("other@example.com")
                .name("Other")
                .lastName("User")
                .password("Pass22042010!")
                .build();
        entityManager.persistAndFlush(user);
        entityManager.persistAndFlush(otherUser);

        Application a1 = createApplication(user);
        Application a2 = createApplication(user);
        Application a3 = createApplication(user);
        Application otherUsersApplication = createApplication(otherUser);

        applicationRepository.save(a1);
        applicationRepository.save(a2);
        applicationRepository.save(a3);
        applicationRepository.save(otherUsersApplication);

        applicationRepository.deleteAllByUser(user);

        entityManager.flush();
        entityManager.clear();

        Assertions.assertTrue(applicationRepository.findById(a1.getId()).isEmpty());
        Assertions.assertTrue(applicationRepository.findById(a2.getId()).isEmpty());
        Assertions.assertTrue(applicationRepository.findById(a3.getId()).isEmpty());
        Assertions.assertTrue(applicationRepository.findById(otherUsersApplication.getId()).isPresent());
    }

    private void assertApplicationFields(Application expected, Application actual) {
        Assertions.assertTrue(actual.getId() > 0);
        Assertions.assertEquals(expected.getCompany(), actual.getCompany());
        Assertions.assertEquals(expected.getPosition(), actual.getPosition());
        Assertions.assertEquals(expected.getLocation(), actual.getLocation());
        Assertions.assertEquals(0, expected.getSalaryFrom().compareTo(actual.getSalaryFrom()));
        Assertions.assertEquals(0, expected.getSalaryTo().compareTo(actual.getSalaryTo()));
        Assertions.assertEquals(ApplicationStatus.APPLIED, actual.getStatus());
        Assertions.assertEquals(expected.getCurrency(), actual.getCurrency());
        Assertions.assertEquals(expected.getApplicationDate(), actual.getApplicationDate());
        Assertions.assertEquals(expected.getJobUrl(), actual.getJobUrl());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getNotes(), actual.getNotes());
        Assertions.assertNotNull(actual.getCreatedAt());
        Assertions.assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        Assertions.assertEquals(expected.getUser().getId(), actual.getUser().getId());
        Assertions.assertEquals(expected.getUser().getEmail(), actual.getUser().getEmail());
    }

    private static Application createApplication(User user) {
        return Application.builder()
                .company("Test")
                .position("position")
                .location("location")
                .salaryFrom(BigDecimal.valueOf(100))
                .salaryTo(BigDecimal.valueOf(300))
                .currency(Currency.USD)
                .applicationDate(LocalDate.now().plusDays(5))
                .jobUrl("url")
                .description("description")
                .notes("notes")
                .user(user).build();
    }
}
