package com.cchqsa.job_application_tracker.service.Impl;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.repository.ApplicationRepository;
import com.cchqsa.job_application_tracker.repository.UserRepository;
import com.cchqsa.job_application_tracker.service.ApplicationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void addApplication(Application application) {
        applicationRepository.save(application);
    }

    @Override
    public List<Application> getUserApplications(User user) {
        return applicationRepository.getApplicationsByUser(user);
    }

    public List<Application> getRecentApplications(User user, List<Application> applications) {
        if (applications == null) return List.of();
        return applications.stream()
                .sorted(Comparator.comparing(
                        Application::getApplicationDate,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .limit(5)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void deleteByIdAndUser(Long id, User user) {

        Application application = applicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Application not found or access denied"));
        applicationRepository.delete(application);
    }

    @Override
    public Optional<Application> findApplicationById(Long id) {
        return applicationRepository.findById(id);
    }


}
