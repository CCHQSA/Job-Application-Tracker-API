package com.cchqsa.job_application_tracker.service;

import com.cchqsa.job_application_tracker.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findByEmail(String username);

    Optional<User> createUser(String email, String password, String name, String lastName);

    String registerAndGetToken(String email, String password, String name, String lastName);

    void save(User user);

    void deleteUser(User user);
}


