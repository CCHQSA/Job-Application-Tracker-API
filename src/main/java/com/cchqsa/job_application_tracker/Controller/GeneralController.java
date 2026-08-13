package com.cchqsa.job_application_tracker.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

    @GetMapping({"/", "/general"})
    public String general() {
        return "general";
    }
}
