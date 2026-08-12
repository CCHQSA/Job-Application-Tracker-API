package com.cchqsa.job_application_tracker.service;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.User;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    void addApplication(Application application);

    List<Application> getUserApplications(User user);

    public List<Application> getRecentApplications(User user, List<Application> applications);

    void deleteByIdAndUser(Long id, User user);

    Optional<Application> findApplicationById(Long id);
}
