package com.cchqsa.job_application_tracker.service;

import com.cchqsa.job_application_tracker.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findByEmail(String username);
}
