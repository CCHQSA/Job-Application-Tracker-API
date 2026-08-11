package com.cchqsa.job_application_tracker.service.Impl;

import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.repository.UserRepository;
import com.cchqsa.job_application_tracker.service.ApplicationService;
import com.cchqsa.job_application_tracker.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ApplicationService applicationService;

    public UserServiceImpl(UserRepository userRepository, ApplicationService applicationService) {
        this.userRepository = userRepository;
        this.applicationService = applicationService;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
