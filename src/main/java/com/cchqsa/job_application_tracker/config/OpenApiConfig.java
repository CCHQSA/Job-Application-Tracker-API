package com.cchqsa.job_application_tracker.config;

import com.cchqsa.job_application_tracker.Controller.ApplicationController;
import com.cchqsa.job_application_tracker.Controller.AuthController;
import com.cchqsa.job_application_tracker.Controller.GeneralController;
import com.cchqsa.job_application_tracker.Controller.HomeController;
import com.cchqsa.job_application_tracker.Controller.InterviewController;
import com.cchqsa.job_application_tracker.Controller.SettingsController;
import com.cchqsa.job_application_tracker.Controller.StatisticsController;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    static {
        SpringDocUtils.getConfig().addRestControllers(
                ApplicationController.class,
                AuthController.class,
                GeneralController.class,
                HomeController.class,
                InterviewController.class,
                SettingsController.class,
                StatisticsController.class
        );
    }

    @Bean
    public OpenAPI jobApplicationTrackerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Application Tracker")
                        .description("Endpoints for managing job applications, interviews, statistics, and account settings.")
                        .version("v1"))
                .components(new Components()
                        .addSecuritySchemes("cookieAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("JWT_TOKEN")
                                .description("JWT authentication cookie created by the login flow.")));
    }
}
