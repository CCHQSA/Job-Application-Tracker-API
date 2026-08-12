package com.cchqsa.job_application_tracker.Controller;

import com.cchqsa.job_application_tracker.dto.ApplicationRequestDto;
import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.enums.ApplicationStatus;
import com.cchqsa.job_application_tracker.service.ApplicationService;
import com.cchqsa.job_application_tracker.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ApplicationController {

    private final UserService userService;
    private final ApplicationService applicationService;

    public ApplicationController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
    }


    @GetMapping("/applications")
    public String listApplications(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Application> applications = applicationService.getUserApplications(user);
        model.addAttribute("activeTab", "applications");
        model.addAttribute("applications", applications);
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

        applicationService.addApplication(application);
    }
}
