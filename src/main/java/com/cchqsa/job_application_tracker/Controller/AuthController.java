package com.cchqsa.job_application_tracker.Controller;

import com.cchqsa.job_application_tracker.dto.UserDto;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.mapper.ModelMapper;
import com.cchqsa.job_application_tracker.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;
    private final ModelMapper<User, UserDto> userMapper;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, ModelMapper<User, UserDto> userMapper, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("name") String name,
                           @RequestParam("lastName") String lastName,
                           HttpServletResponse response,
                           RedirectAttributes redirectAttributes) {
        try {
            String token = userService.registerAndGetToken(email, password, name, lastName);
            Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(86400);
            response.addCookie(jwtCookie);
            return "redirect:/home";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authAttempt =
                new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authAttempt);
        return "redirect:/home";

    }
}


