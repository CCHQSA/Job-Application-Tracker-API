package com.cchqsa.job_application_tracker.Controller;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.Interview;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.enums.InterviewStatus;
import com.cchqsa.job_application_tracker.enums.InterviewType;
import com.cchqsa.job_application_tracker.service.ApplicationService;
import com.cchqsa.job_application_tracker.service.InterviewService;
import com.cchqsa.job_application_tracker.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class InterviewController {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final InterviewService interviewService;

    public InterviewController(UserService userService, ApplicationService applicationService, InterviewService interviewService) {
        this.userService = userService;
        this.applicationService = applicationService;
        this.interviewService = interviewService;
    }

    @GetMapping("/interviews")
    public String interviews(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Application> applications = applicationService.getUserApplications(user);
        List<Interview> allInterviews = interviewService.findAllByApplicationIn(applications);

        List<Interview> upcomingInterviews = allInterviews.stream()
                .filter(interview -> interview.getDateTime().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Interview::getDateTime))
                .collect(Collectors.toList());

        List<Long> applicationsWithInterviewsIds = allInterviews.stream()
                .map(interview -> interview.getApplication().getId())
                .distinct()
                .collect(Collectors.toList());

        List<Application> applicationsWithoutInterviews = applications.stream()
                .filter(app -> !applicationsWithInterviewsIds.contains(app.getId()))
                .collect(Collectors.toList());

        model.addAttribute("allInterviews", allInterviews);
        model.addAttribute("upcomingInterviews", upcomingInterviews);
        model.addAttribute("applicationsWithoutInterviews", applicationsWithoutInterviews);

        model.addAttribute("allApplicationsList", applications);
        model.addAttribute("hasApplications", !applications.isEmpty());
        model.addAttribute("activeTab", "interviews");

        return "interviews";
    }

    @GetMapping("/interviews/add")
    public String addInterview(
            @RequestParam(name = "applicationId", required = false) Long applicationId,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Application> applications = applicationService.getUserApplications(user);

        Interview interview = new Interview();
        model.addAttribute("interview", interview);
        model.addAttribute("allApplicationsList", applications);
        model.addAttribute("activeTab", "interviews");

        if (applicationId != null) {
            Application application = applicationService.findApplicationById(applicationId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + applicationId));
            if (!application.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Invalid application ID: " + applicationId);
            }
            model.addAttribute("application", application);
            model.addAttribute("applicationCompany", application.getCompany());
            model.addAttribute("applicationPosition", application.getPosition());
        }

        return "add-interview";
    }

    @PostMapping("/interviews/save")
    public String saveInterview(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(name = "applicationId", required = false) Long applicationId,
                                @RequestParam(name = "type", required = false) String typeValue,
                                @RequestParam(name = "status", required = false) String statusValue,
                                @RequestParam(name = "dateTime", required = false) String dateTimeValue,
                                @RequestParam(name = "interviewer", required = false) String interviewer,
                                @RequestParam(name = "location", required = false) String location,
                                @RequestParam(name = "meetingUrl", required = false) String meetingUrl,
                                @RequestParam(name = "notes", required = false) String notes,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Application> applications = applicationService.getUserApplications(user);
        model.addAttribute("allApplicationsList", applications);

        if (applicationId == null) {
            model.addAttribute("error", "Application is required.");
            model.addAttribute("activeTab", "interviews");
            return "add-interview";
        }

        Application application = applicationService.findApplicationById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + applicationId));

        if (!application.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Invalid application ID: " + applicationId);
        }

        InterviewType type;
        InterviewStatus status;
        LocalDateTime dateTime;
        try {
            type = InterviewType.valueOf(typeValue);
            status = InterviewStatus.valueOf(statusValue);
            dateTime = LocalDateTime.parse(dateTimeValue);
        } catch (IllegalArgumentException | DateTimeParseException | NullPointerException e) {
            addInterviewModel(model, application);
            model.addAttribute("error", "Please choose a valid interview type, status, and date/time.");
            return "add-interview";
        }

        if (interviewer == null || interviewer.isBlank()) {
            addInterviewModel(model, application);
            model.addAttribute("error", "Interviewer name is required.");
            return "add-interview";
        }

        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setType(type);
        interview.setStatus(status);
        interview.setDateTime(dateTime);
        interview.setInterviewer(interviewer);
        interview.setLocation(location);
        interview.setMeetingUrl(meetingUrl);
        interview.setNotes(notes);

        interviewService.save(interview);

        redirectAttributes.addFlashAttribute("success", "Interview saved successfully.");
        return "redirect:/interviews";
    }

    @PostMapping("/interviews/delete")
    public String deleteInterview(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam("interviewId") Long interviewId,
                                  RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Interview interview = interviewService.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid interview ID: " + interviewId));

        if (!interview.getApplication().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Access denied");
        }

        interviewService.delete(interview);
        redirectAttributes.addFlashAttribute("success", "Interview deleted successfully.");
        return "redirect:/interviews";
    }

    private void addInterviewModel(Model model, Application application) {
        model.addAttribute("application", application);
        model.addAttribute("applicationCompany", application.getCompany());
        model.addAttribute("applicationPosition", application.getPosition());
        model.addAttribute("activeTab", "interviews");
    }
}
