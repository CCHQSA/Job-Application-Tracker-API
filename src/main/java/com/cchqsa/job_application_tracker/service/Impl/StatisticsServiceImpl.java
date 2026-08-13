package com.cchqsa.job_application_tracker.service.Impl;

import com.cchqsa.job_application_tracker.dto.StatisticsDto;
import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.Interview;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.enums.ApplicationStatus;
import com.cchqsa.job_application_tracker.enums.InterviewStatus;
import com.cchqsa.job_application_tracker.service.ApplicationService;
import com.cchqsa.job_application_tracker.service.InterviewService;
import com.cchqsa.job_application_tracker.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final ApplicationService applicationService;
    private final InterviewService interviewService;

    private static final Set<ApplicationStatus> ACTIVE_STATUSES = Set.of(
            ApplicationStatus.APPLIED, ApplicationStatus.SCREENING,
            ApplicationStatus.INTERVIEW, ApplicationStatus.TECHNICAL_INTERVIEW
    );

    public StatisticsServiceImpl(ApplicationService applicationService, InterviewService interviewService) {
        this.applicationService = applicationService;
        this.interviewService = interviewService;
    }

    @Override
    public StatisticsDto getUserStatistics(User user) {
        List<Application> applications = applicationService.getUserApplications(user);
        List<Interview> interviews = interviewService.findByApplicationIn(applications);

        long totalApps = applications.size();

        List<Application> activeApplications = applications.stream()
                .filter(app -> ACTIVE_STATUSES.contains(app.getStatus()))
                .toList();

        Map<ApplicationStatus, Long> sortedStatusCounts = filterAndSortStatusCounts(applications);

        double interviewRate = 0.0;
        double offeredRate = 0.0;
        double rejectedRate = 0.0;

        if (totalApps > 0) {
            long interviewApps = interviews.stream()
                    .map(interview -> interview.getApplication().getId())
                    .distinct()
                    .count();
            long offeredApps = applications.stream().filter(app -> app.getStatus() == ApplicationStatus.OFFER).count();
            long rejectedApps = applications.stream().filter(app -> app.getStatus() == ApplicationStatus.REJECTED).count();

            interviewRate = roundToOneDecimal(((double) interviewApps / totalApps) * 100);
            offeredRate = roundToOneDecimal(((double) offeredApps / totalApps) * 100);
            rejectedRate = roundToOneDecimal(((double) rejectedApps / totalApps) * 100);
        }

        long scheduled = countByInterviewStatus(interviews, InterviewStatus.SCHEDULED);
        long completed = countByInterviewStatus(interviews, InterviewStatus.COMPLETED);
        long rescheduled = countByInterviewStatus(interviews, InterviewStatus.RESCHEDULED);
        long cancelled = countByInterviewStatus(interviews, InterviewStatus.CANCELLED);

        Map<String, Long> sortedLocationCounts = filterAndSortLocationCounts(applications);

        return new StatisticsDto(
                totalApps, interviews.size(), activeApplications, sortedStatusCounts,
                interviewRate, offeredRate, rejectedRate, scheduled, completed, rescheduled, cancelled, sortedLocationCounts
        );
    }

    @Override
    public List<Application> getActiveApplications(User user) {
        return applicationService.getUserApplications(user).stream()
                .filter(app -> ACTIVE_STATUSES.contains(app.getStatus()))
                .toList();
    }

    @Override
    public Map<ApplicationStatus, Long> getApplicationStatusCounts(User user) {
        List<Application> applications = applicationService.getUserApplications(user);
        return filterAndSortStatusCounts(applications);
    }

    @Override
    public Map<String, Long> getApplicationLocationCounts(User user) {
        List<Application> applications = applicationService.getUserApplications(user);
        return filterAndSortLocationCounts(applications);
    }

    private Map<ApplicationStatus, Long> filterAndSortStatusCounts(List<Application> applications) {
        return applications.stream()
                .collect(Collectors.groupingBy(Application::getStatus, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<ApplicationStatus, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
    }

    private Map<String, Long> filterAndSortLocationCounts(List<Application> applications) {
        return applications.stream()
                .collect(Collectors.groupingBy(Application::getLocation, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private long countByInterviewStatus(List<Interview> interviews, InterviewStatus status) {
        if (interviews == null) {
            return 0;
        }

        return interviews.stream()
                .filter(interview -> interview.getStatus() == status)
                .count();
    }
}
