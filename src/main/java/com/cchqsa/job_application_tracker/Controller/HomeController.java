package com.cchqsa.job_application_tracker.Controller;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.service.ApplicationService;
import com.cchqsa.job_application_tracker.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final ApplicationService applicationService;

    public HomeController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        List<Application> applications = applicationService.getUserApplications(user);
        List<Application> recentApplications = applicationService.getRecentApplications(user, applications);

        model.addAttribute("activeTab", "home");

        model.addAttribute("totalApplications", applications.size());
        model.addAttribute("recentApplications", recentApplications);
        model.addAttribute("user", user);
        return "home";
    }

}
