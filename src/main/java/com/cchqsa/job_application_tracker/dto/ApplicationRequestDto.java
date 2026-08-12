package com.cchqsa.job_application_tracker.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ApplicationRequestDto {
    private String company;
    private String position;
    private BigDecimal salaryFrom;
    private BigDecimal salaryTo;
    private String location;
    private LocalDate applicationDate;
}
