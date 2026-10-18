package com.cchqsa.job_application_tracker.repository_tests;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.enums.ApplicationStatus;
import com.cchqsa.job_application_tracker.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void save_persistsUserAndGeneratedId() {
        User user = User.builder()
                .email("testemail@gmail.com")
                .name("Name").lastName("lastName")
                .password("Pass22042010!")
                .build();
        User savedUser = userRepository.save(user);
        entityManager.flush();
        Long savedId = savedUser.getId();
        entityManager.clear();

        User reloaded = userRepository.findById(savedId).orElseThrow();
        Assertions.assertTrue(savedId > 0);
        Assertions.assertEquals(user.getEmail(), reloaded.getEmail());
        Assertions.assertEquals(user.getName(), reloaded.getName());
        Assertions.assertEquals(user.getLastName(), reloaded.getLastName());
        Assertions.assertEquals(user.getPassword(), reloaded.getPassword());
    }

    @Test
    public void findAll_returnsPersistedUsers() {
        User user1 = User.builder()
                .email("testemail1@gmail.com")
                .name("Name1").lastName("lastName1")
                .password("Pass22042010!1")
                .build();

        User user2 = User.builder()
                .email("testemail2@gmail.com")
                .name("Name").lastName("lastName2")
                .password("Pass22042010!2")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        entityManager.flush();
        entityManager.clear();

        List<User> actual = userRepository.findAll();

        List<String> actualEmails = actual.stream()
                .map(User::getEmail)
                .sorted()
                .toList();
        Assertions.assertEquals(List.of("testemail1@gmail.com", "testemail2@gmail.com"), actualEmails);
    }

    @Test
    public void findById_returnsSavedUser() {
        User expected = User.builder()
                .email("testemail@gmail.com")
                .name("Name").lastName("lastName")
                .password("Pass22042010!")
                .build();

        userRepository.save(expected);
        entityManager.flush();
        Long userId = expected.getId();
        entityManager.clear();

        User actual = userRepository.findById(userId).orElseThrow();

        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
    }

    @Test
    public void findByEmail_returnsMatchingUser() {
        User expected = User.builder()
                .email("testemail@gmail.com")
                .name("Name").lastName("lastName")
                .password("Pass22042010!")
                .build();
        userRepository.save(expected);
        entityManager.flush();
        entityManager.clear();

        User actual = userRepository.findByEmail(expected.getEmail()).orElseThrow();

        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    public void findByEmail_returnsEmptyForUnknownEmail() {
        Assertions.assertTrue(userRepository.findByEmail("missing@example.com").isEmpty());
    }

    @Test
    public void delete_removesUser() {
        User expected = User.builder()
                .email("testemail@gmail.com")
                .name("Name").lastName("lastName")
                .password("Pass22042010!")
                .build();
        userRepository.save(expected);

        userRepository.delete(expected);

        entityManager.flush();
        entityManager.clear();

        Assertions.assertTrue(userRepository.findById(expected.getId()).isEmpty());
    }

    @Test
    public void deleteByIdAndUser_deletesOnlyWhenUserOwnsApplication() {
        User owner = userRepository.save(createUser("owner@example.com"));
        User anotherUser = userRepository.save(createUser("other@example.com"));
        Application application = createApplication(owner);
        entityManager.persist(application);
        entityManager.flush();
        Long applicationId = application.getId();

        userRepository.deleteByIdAndUser(applicationId, anotherUser);
        entityManager.flush();
        Assertions.assertTrue(entityManager.find(Application.class, applicationId) != null);

        userRepository.deleteByIdAndUser(applicationId, owner);
        entityManager.flush();
        entityManager.clear();
        Assertions.assertNull(entityManager.find(Application.class, applicationId));
    }

    private User createUser(String email) {
        return User.builder()
                .email(email)
                .name("Test")
                .lastName("User")
                .password("Pass22042010!")
                .build();
    }

    private Application createApplication(User user) {
        Application application = new Application();
        application.setCompany("Company");
        application.setPosition("Engineer");
        application.setLocation("Remote");
        application.setSalaryFrom(BigDecimal.valueOf(50000));
        application.setSalaryTo(BigDecimal.valueOf(80000));
        application.setStatus(ApplicationStatus.APPLIED);
        application.setApplicationDate(LocalDate.now());
        application.setUser(user);
        return application;
    }
}
