package com.cchqsa.job_application_tracker.Controller;

import com.cchqsa.job_application_tracker.dto.StatisticsDto;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.service.StatisticsService;
import com.cchqsa.job_application_tracker.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {

    private final UserService userService;
    private final StatisticsService statisticsService;

    public StatisticsController(UserService userService, StatisticsService statisticsService) {
        this.userService = userService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public String statistics(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        StatisticsDto stats = statisticsService.getUserStatistics(user);

        model.addAttribute("activeTab", "statistics");
        model.addAttribute("totalApplications", stats.totalApplications());
        model.addAttribute("totalInterviews", stats.totalInterviews());
        model.addAttribute("activeApplications", stats.activeApplications());
        model.addAttribute("applicationStatus", stats.applicationStatus());
        model.addAttribute("interviewRate", stats.interviewRate());
        model.addAttribute("offeredRate", stats.offeredRate());
        model.addAttribute("rejectedRate", stats.rejectedRate());
        model.addAttribute("scheduledInterviews", stats.scheduledInterviews());
        model.addAttribute("completedInterviews", stats.completedInterviews());
        model.addAttribute("rescheduledInterviews", stats.rescheduledInterviews());
        model.addAttribute("cancelledInterviews", stats.cancelledInterviews());
        model.addAttribute("applicationLocations", stats.applicationLocations());

        return "statistics";
    }
}
