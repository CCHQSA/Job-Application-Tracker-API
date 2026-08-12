package com.cchqsa.job_application_tracker.service.Impl;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.repository.ApplicationRepository;
import com.cchqsa.job_application_tracker.repository.UserRepository;
import com.cchqsa.job_application_tracker.service.ApplicationService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    @Override
    public List<Application> getRecentApplications(User user, List<Application> applications) {
        if (applications == null || user == null) {
            return Collections.emptyList();
        }

        return applications.stream()
                .filter(app -> app.getUser() != null && app.getUser().getId().equals(user.getId()))
                .sorted(Comparator.comparing(Application::getCreatedAt).reversed())
                .limit(3)
                .toList(); // Для Java 16+. Якщо версія нижча, використовуйте .collect(Collectors.toList())
    }


}
