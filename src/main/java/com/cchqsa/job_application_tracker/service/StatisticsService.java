package com.cchqsa.job_application_tracker.service;

import com.cchqsa.job_application_tracker.dto.StatisticsDto;
import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.Interview;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.enums.ApplicationStatus;
import com.cchqsa.job_application_tracker.enums.InterviewStatus;

import java.util.List;
import java.util.Map;

public interface StatisticsService {

    StatisticsDto getUserStatistics(User user);

    List<Application> getActiveApplications(User user);

    Map<ApplicationStatus, Long> getApplicationStatusCounts(User user);

    Map<String, Long> getApplicationLocationCounts(User user);

    default double roundToOnesDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    default long countInterviewsByStatus(List<Interview> interviews, InterviewStatus status) {
        if (interviews == null) return 0;
        return interviews.stream()
                .filter(i -> i.getStatus() == status)
                .count();
    }
}
