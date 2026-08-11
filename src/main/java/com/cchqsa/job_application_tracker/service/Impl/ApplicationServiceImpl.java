package com.cchqsa.job_application_tracker.service.Impl;

import com.cchqsa.job_application_tracker.repository.ApplicationRepository;
import com.cchqsa.job_application_tracker.repository.UserRepository;
import com.cchqsa.job_application_tracker.service.ApplicationService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }
}
