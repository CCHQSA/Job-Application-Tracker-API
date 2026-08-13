package com.cchqsa.job_application_tracker.dto;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.enums.ApplicationStatus;
import java.util.List;
import java.util.Map;

public record StatisticsDto(
        long totalApplications,
        long totalInterviews,
        List<Application> activeApplications,
        Map<ApplicationStatus, Long> applicationStatus,
        double interviewRate,
        double offeredRate,
        double rejectedRate,
        long scheduledInterviews,
        long completedInterviews,
        long rescheduledInterviews,
        long cancelledInterviews,
        Map<String, Long> applicationLocations
) {}
