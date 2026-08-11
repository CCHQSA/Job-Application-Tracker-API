package com.cchqsa.job_application_tracker.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class GeneralController {

    @GetMapping("/")
    public String general() {
        return "general";
    }
}
