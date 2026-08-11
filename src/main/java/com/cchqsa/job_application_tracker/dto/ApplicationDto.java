package com.cchqsa.job_application_tracker.dto;

import com.cchqsa.job_application_tracker.enums.ApplicationStatus;
import com.cchqsa.job_application_tracker.enums.Currencies;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicationDto {

    private Long id;

    private String company;

    private String position;

    private String location;

    private BigDecimal salaryFrom;

    private BigDecimal salaryTo;

    private ApplicationStatus status;

    private Currencies currency;

    private LocalDate applicationDate;

    private String jobUrl;

    private String description;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}