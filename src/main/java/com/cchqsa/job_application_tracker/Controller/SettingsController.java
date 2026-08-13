package com.cchqsa.job_application_tracker.Controller;

import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SettingsController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/settings")
    public String settings(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("activeTab", "settings");
        return "settings";
    }

    @PostMapping("/settings/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam("name") String name,
                          @RequestParam("lastName") String lastName,
                          RedirectAttributes redirectAttributes
    ) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(name);
        user.setLastName(lastName);
        userService.save(user);
        redirectAttributes.addFlashAttribute("profileSuccess", "Profile settings updated successfully.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("currentPassword") String currPass,
                                 @RequestParam("newPassword") String newPass,
                                 @RequestParam("confirmPassword") String confirmPass,
                                 RedirectAttributes redirectAttributes
    ) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(currPass, user.getPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "Current password is incorrect.");
            return "redirect:/settings";
        }

        if (!newPass.equals(confirmPass)) {
            redirectAttributes.addFlashAttribute("passwordError", "New password and confirmation do not match.");
            return "redirect:/settings";
        }

        if (newPass.length() < 8) {
            redirectAttributes.addFlashAttribute("passwordError", "Password must be at least 8 characters long.");
            return "redirect:/settings";
        }

        user.setPassword(passwordEncoder.encode(newPass));
        userService.save(user);

        redirectAttributes.addFlashAttribute("passwordSuccess", "Password changed successfully.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/delete-account")
    public String deleteAccount(@AuthenticationPrincipal UserDetails userDetails,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                RedirectAttributes redirectAttributes
    ) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.deleteUser(user);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie jwtCookie = new Cookie("JWT_TOKEN", null);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        Cookie jsessionCookie = new Cookie("JSESSIONID", null);
        jsessionCookie.setPath("/");
        jsessionCookie.setMaxAge(0);
        response.addCookie(jsessionCookie);

        SecurityContextHolder.clearContext();

        redirectAttributes.addFlashAttribute("deleteSuccess", "Account deleted successfully.");
        return "redirect:/general";
    }

}
