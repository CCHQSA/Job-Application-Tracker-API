package com.cchqsa.job_application_tracker.Controller;

import com.cchqsa.job_application_tracker.dto.ApplicationRequestDto;
import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.Interview;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.enums.ApplicationStatus;
import com.cchqsa.job_application_tracker.service.ApplicationService;
import com.cchqsa.job_application_tracker.service.InterviewService;
import com.cchqsa.job_application_tracker.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ApplicationController {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final InterviewService interviewService;

    public ApplicationController(UserService userService, ApplicationService applicationService, InterviewService interviewService) {
        this.userService = userService;
        this.applicationService = applicationService;
        this.interviewService = interviewService;
    }


    @GetMapping("/applications")
    public String listApplications(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Application> applications = applicationService.getUserApplications(user);
        List<Interview> totalInterviews = interviewService.findByApplicationIn(applications);
        List<Application> pendingApplications = applications.stream()
                .filter(app -> app.getStatus() != ApplicationStatus.OFFER
                        && app.getStatus() != ApplicationStatus.REJECTED
                        && app.getStatus() != ApplicationStatus.WITHDRAWN)
                .toList();
        model.addAttribute("activeTab", "applications");
        model.addAttribute("applications", applications);
        model.addAttribute("totalInterviews", totalInterviews.size());
        model.addAttribute("pendingApplications", pendingApplications.size());

        return "applications";
    }

    @PostMapping("/applications")
    public String addToApplications(@AuthenticationPrincipal UserDetails userDetails,
                                    @ModelAttribute ApplicationRequestDto dto,
                                    @RequestParam(name = "sourcePage", defaultValue = "applications") String sourcePage) {
        saveApplicationFromDto(userDetails, dto);

        if ("home".equals(sourcePage)) {
            return "redirect:/home";
        }

        return "redirect:/applications";
    }

    @Transactional
    @PostMapping("/applications/delete/{id}")
    public String deleteApplication(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        applicationService.deleteByIdAndUser(id, userService.findByEmail(userDetails.getUsername()).get());
        return "redirect:/applications";
    }

    @PostMapping("/applications/change-status/{id}")
    public String changeApplicationStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("id") Long id,
            @RequestParam("status") ApplicationStatus status
            ){
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Application application = applicationService.findApplicationById(id).get();
        LocalDateTime now = LocalDateTime.now();
        application.setUpdatedAt(now);
        application.setStatus(status);
        applicationService.addApplication(application);
        return "redirect:/applications";
    }


    private void saveApplicationFromDto(UserDetails userDetails, ApplicationRequestDto dto) {
        User currUser = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Application application = new Application();
        application.setCompany(dto.getCompany());
        application.setPosition(dto.getPosition());
        application.setSalaryFrom(dto.getSalaryFrom());
        application.setSalaryTo(dto.getSalaryTo());
        application.setLocation(dto.getLocation());
        application.setApplicationDate(dto.getApplicationDate());
        application.setUser(currUser);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setCurrency(dto.getCurrency());
        application.setJobUrl(dto.getJobUrl());
        application.setDescription(dto.getDescription());
        application.setNotes(dto.getNotes());
        applicationService.addApplication(application);
    }
}
