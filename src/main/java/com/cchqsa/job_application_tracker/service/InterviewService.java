package com.cchqsa.job_application_tracker.service;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.Interview;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InterviewService {

    List<Interview> findByApplicationIn(List<Application> applications);

    List<Interview> findAllByApplicationIn(List<Application> applications);

    Interview save(Interview interview);

    @Transactional
    void delete(Interview interview);

    Optional<Interview> findById(Long interviewId);

    void saveInterview(Interview existingInterview, Application application);

    void deleteAllByApplications(List<Application> applicationList);
}
