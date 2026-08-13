package com.cchqsa.job_application_tracker.service.Impl;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.enums.Role;
import com.cchqsa.job_application_tracker.repository.UserRepository;
import com.cchqsa.job_application_tracker.security.CustomUserDetails;
import com.cchqsa.job_application_tracker.security.jwt.JwtService;
import com.cchqsa.job_application_tracker.service.ApplicationService;
import com.cchqsa.job_application_tracker.service.InterviewService;
import com.cchqsa.job_application_tracker.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ApplicationService applicationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final InterviewService interviewService;

    @PersistenceContext
    private EntityManager entityManager;

    public UserServiceImpl(UserRepository userRepository, ApplicationService applicationService, PasswordEncoder passwordEncoder, JwtService jwtService, InterviewService interviewService) {
        this.userRepository = userRepository;
        this.applicationService = applicationService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.interviewService = interviewService;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> createUser(String email, String password, String name, String lastName) {
        if (userRepository.findByEmail(email).isPresent()) {
            return Optional.empty();
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ROLE_USER);
        return Optional.of(userRepository.save(user));
    }

    public String registerAndGetToken(String email, String password, String name, String lastName) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.ROLE_USER);

        User savedUser = userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        return jwtService.generateToken(userDetails);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<Application> applicationList = applicationService.getUserApplications(user);

        if (applicationList != null && !applicationList.isEmpty()) {
            interviewService.deleteAllByApplications(applicationList);
        }

        applicationService.deleteAllByUser(user);

        entityManager.clear();

        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(managedUser);
    }
}
